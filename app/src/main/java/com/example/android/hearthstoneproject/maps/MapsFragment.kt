package com.example.android.hearthstoneproject.maps

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.android.hearthstoneproject.R
import com.example.android.hearthstoneproject.network.repo.HearthStoneRepo
import com.example.android.hearthstoneproject.util.createViewModel
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.model.PlaceLikelihood
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.android.synthetic.main.fragment_modal_bottom_sheet.view.*


class MapsFragment : Fragment() {

    private val viewModel: MapsViewModel by lazy {
        createViewModel {
            MapsViewModel(
                application = this.requireActivity().application,
                HearthStoneRepo.provideHeartStoneRepo()
            )
        }
    }

    private val TAG = MapsFragment::class.java.simpleName

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    private lateinit var googleMap: GoogleMap

    private lateinit var placesClient: PlacesClient

    private lateinit var currentLatLng: LatLng

    private var lastMarker: Marker? = null

    private val callback = OnMapReadyCallback {

        // Initialize the SDK
        Places.initialize(this.requireContext(), "AIzaSyCiJxfTYJfP57FLWYGYmmDjikgCpaBhXjs")


        // Create a new PlacesClient instance
        placesClient = Places.createClient(this.requireContext())

        test()

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */

        googleMap = it

        googleMap.uiSettings.isZoomControlsEnabled = true

        setMapStyle(googleMap)

        googleMap.setOnMarkerClickListener { marker ->
            onMarkerClick(marker)
        }

        setUpMap()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(this.requireActivity())

//        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
//            PackageManager.PERMISSION_GRANTED) {
//
//            val placeFields: List<Place.Field> = listOf(Place.Field.NAME)
//
//// Use the builder to create a FindCurrentPlaceRequest.
//            val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)
//
//            val placeResponse = placesClient.findCurrentPlace(request)
//            placeResponse.addOnCompleteListener { task ->
//                if (task.isSuccessful) {
//                    val response = task.result
//                    for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods ?: emptyList()) {
//                        Log.d("Zelda", "Place '${placeLikelihood.place.name}' has likelihood: ${placeLikelihood.likelihood}")
//                    }
//                } else {
//                    val exception = task.exception
//                    if (exception is ApiException) {
//                        Log.e(TAG, "Place not found: ${exception.statusCode}")
//                    }
//                }
//            }
//        }

        viewModel.storeFeed.observe(viewLifecycleOwner, {
            it?.results?.forEach { stores ->
                val storeLatLng = LatLng(stores.geometry.location.lat, stores.geometry.location.lng)
                val snippet = getAddress(storeLatLng)
                val storeMarker = googleMap.addMarker(
                    MarkerOptions()
                        .position(storeLatLng)
                        .title(stores.name)
                        .snippet(snippet)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.small_hearthstone_logo))
                )
                Log.d("Zelda", "New marker has been made. " +
                        "${storeMarker.title} is located in ${storeMarker.position}")
                storeMarker.tag = false
            }
        })

        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        val clickCount = marker.tag as? Boolean

        val distanceLocation = Location("")

        distanceLocation.longitude = marker.position.longitude
        distanceLocation.latitude = marker.position.latitude

        val distance = lastLocation.distanceTo(distanceLocation) / 1000
        Log.d("Zelda", "Distance is $distance")


        if(clickCount == false)
        {
            Log.d("Zelda","This marker coordinates: ${marker.snippet}")

            lastMarker?.tag = false
            lastMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.small_hearthstone_logo))
            lastMarker = marker
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hearthstone_logo))
            marker.tag = true
        }

        val modalSheetView = layoutInflater.inflate(R.layout.fragment_modal_bottom_sheet,null)
        modalSheetView.locationTitle.text = marker.title
        modalSheetView.locationAddress.text = marker.snippet
        modalSheetView.distanceTextView.text = distance.toString() + " KM"
        val dialog = BottomSheetDialog(this.requireContext())
        dialog.setContentView(modalSheetView)
        dialog.show()

        // Return false to indicate that we have not consumed the event and that we wish
        // for the default behavior to occur (which is for the camera to move such that the
        // marker is centered and for the marker's info window to open, if it has one).
        return true
    }

    private fun setMapStyle(map: GoogleMap) {
        try {
            val success = map.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(
                    this.requireContext(),
                    R.raw.map_style
                )
            )
            if (!success){
                Log.e(TAG, "Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Log.e(TAG, "Cant find style. Error: ", e)
        }
    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this.requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this.requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        googleMap.isMyLocationEnabled = true


        fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->

            if (location != null) {
                lastLocation = location
                currentLatLng = LatLng(location.latitude, location.longitude)
                val locationString = "${location.latitude},${location.longitude}"
                googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                viewModel.getStores(locationString, 100000, "book_store")
                Log.d("Zelda", "Made the map")

            }
        }
    }

    private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(this.requireContext())
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude,1)
        return list[0].getAddressLine(0)
    }

    private fun test(){
        // Use fields to define the data types to return.
        val placeFields: List<Place.Field> = listOf(Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG)

// Use the builder to create a FindCurrentPlaceRequest.
        val request: FindCurrentPlaceRequest = FindCurrentPlaceRequest.newInstance(placeFields)

// Call findCurrentPlace and handle the response (first check that the user has granted permission).
        if (ContextCompat.checkSelfPermission(this.requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
            PackageManager.PERMISSION_GRANTED) {

            val placeResponse = placesClient.findCurrentPlace(request)

            placeResponse.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val response = task.result
                    for (placeLikelihood: PlaceLikelihood in response?.placeLikelihoods ?: emptyList()) {
                        Log.i(
                            TAG,
                            "Place '${placeLikelihood.place.name}' has " +
                                    "address: ${placeLikelihood.place.address} and " +
                                    "likelihood: ${placeLikelihood.likelihood} and " +
                                    "LatLng ${placeLikelihood.place.latLng}"
                        )
                    }
                } else {
                    val exception = task.exception
                    if (exception is ApiException) {
                        Log.e(TAG, "Place not found: ${exception.statusCode}")
                    }
                }
            }
        }
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }

}