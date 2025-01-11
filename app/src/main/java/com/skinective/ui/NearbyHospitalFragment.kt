package com.skinective.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.skinective.R
import com.skinective.databinding.FragmentHospitalBinding
import com.skinective.network.responses.Hospital
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import com.android.volley.RequestQueue

class NearbyHospitalFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var placesClient: PlacesClient
    private lateinit var requestQueue: RequestQueue
    private lateinit var hospitalAdapter: HospitalAdapter
    private val hospitalList = mutableListOf<Hospital>()
    private var userLocation: LatLng? = null

    private var _binding: FragmentHospitalBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHospitalBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize the map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        // Initialize Places
        Places.initialize(requireContext(), getString(R.string.google_maps_api))
        placesClient = Places.createClient(requireContext())

        // Initialize Request Queue
        requestQueue = Volley.newRequestQueue(requireContext())

        // Initialize RecyclerView
        binding.rvHospitals.layoutManager = LinearLayoutManager(requireContext())
        hospitalAdapter = HospitalAdapter(hospitalList)
        binding.rvHospitals.adapter = hospitalAdapter
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        enableMyLocation()
    }

    private fun enableMyLocation() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            mMap.isMyLocationEnabled = true
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
    }

    private fun getDeviceLocation() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        try {
            val locationResult = fusedLocationProviderClient.lastLocation
            locationResult.addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null) {
                    val location = task.result
                    userLocation = LatLng(location.latitude, location.longitude)
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation!!, 15f))
                    findNearbyHospitals(userLocation!!)
                } else {
                    // Handle location null case
                }
            }
        } catch (e: SecurityException) {
            e.printStackTrace()
        }
    }

    private fun findNearbyHospitals(location: LatLng) {
        val apiKey = getString(R.string.google_maps_api)
        val url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json?" +
                "location=${location.latitude},${location.longitude}" +
                "&radius=2000&type=hospital&key=$apiKey"

        val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
            try {
                val results = response.getJSONArray("results")
                for (i in 0 until results.length()) {
                    val place = results.getJSONObject(i)
                    val name = place.getString("name")
                    val vicinity = place.getString("vicinity")
                    val lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat")
                    val lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng")

                    // Example values for opening hours and distance
                    val openingHours = "Open 24 hours"
                    val distance = "2 km"

                    val hospital = Hospital(name, vicinity, LatLng(lat, lng), openingHours, distance, "")
                    hospitalList.add(hospital)

                    // Add marker to map
                    mMap.addMarker(MarkerOptions().position(LatLng(lat, lng)).title(name))
                }
                hospitalAdapter.notifyDataSetChanged()

                // Fetch travel times for each hospital
                fetchTravelTimes()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }, {
            it.printStackTrace()
        })

        requestQueue.add(request)
    }

    private fun fetchTravelTimes() {
        val apiKey = getString(R.string.google_maps_api)
        userLocation?.let { userLoc ->
            hospitalList.forEach { hospital ->
                val destination = hospital.location
                val url = "https://maps.googleapis.com/maps/api/directions/json?" +
                        "origin=${userLoc.latitude},${userLoc.longitude}&destination=${destination.latitude},${destination.longitude}&key=$apiKey"

                val request = JsonObjectRequest(Request.Method.GET, url, null, { response ->
                    try {
                        val routes = response.getJSONArray("routes")
                        if (routes.length() > 0) {
                            val legs = routes.getJSONObject(0).getJSONArray("legs")
                            if (legs.length() > 0) {
                                val duration = legs.getJSONObject(0).getJSONObject("duration").getString("text")
                                hospital.travelTime = duration
                            }
                        }
                        hospitalAdapter.notifyDataSetChanged()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }, {
                    it.printStackTrace()
                })

                requestQueue.add(request)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // Handle location permission request result
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableMyLocation()
            }
        }
    }
}
