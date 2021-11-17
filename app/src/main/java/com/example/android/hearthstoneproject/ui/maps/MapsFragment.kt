package com.example.android.hearthstoneproject.ui.maps

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import com.example.android.hearthstoneproject.R
import com.example.android.hearthstoneproject.databinding.FragmentMapsBinding
import com.example.android.hearthstoneproject.secret.API.API_KEY
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.blurry.Blurry
import kotlinx.android.synthetic.main.fragment_modal_bottom_sheet.view.*
import timber.log.Timber
import java.math.BigDecimal
import java.math.RoundingMode

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private val viewModel: MapsViewModel by viewModels()

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var lastLocation: Location

    private lateinit var googleMap: GoogleMap

    private lateinit var mapView: MapView

    private lateinit var placesClient: PlacesClient

    private lateinit var currentLatLng: LatLng

    private var lastMarker: Marker? = null

    private lateinit var binding: FragmentMapsBinding

    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->

            if (isGranted) {
                Timber.d("Permission granted by the user")
                enableMyLocation()
                binding.mapsFragmentBackgroundIv.visibility = View.INVISIBLE
            } else {
                Timber.d("Permission denied by the user")
                view?.findNavController()?.navigate(
                    MapsFragmentDirections.actionMapsFragmentToMainScreenFragment()
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentMapsBinding.inflate(inflater)

        val v = binding.root

        fusedLocationClient = LocationServices
            .getFusedLocationProviderClient(this.requireActivity())

        Places.initialize(this.requireContext(), API_KEY)

        placesClient = Places.createClient(this.requireContext())

        mapView = v.findViewById(R.id.map) as MapView
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

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
                if (storeMarker != null) {
                    Timber.d("New marker has been made. ${storeMarker.title} is located in ${storeMarker.position}")
                    storeMarker.tag = false
                }
            }
        })

        return v
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (ContextCompat.checkSelfPermission(requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            Timber.d("Getting permissions")
        }
        else{
            Timber.d("Permissions already granted.")
        }
    }

    override fun onMapReady(it: GoogleMap) {

        if(!isPermissionGranted())
        {
            binding.mapsFragmentBackgroundIv.visibility = View.VISIBLE
            Blurry.with(requireContext())
                .radius(3)
                .sampling(8)
                .async()
                .capture(binding.mapsFragmentBackgroundIv)
                .into(binding.mapsFragmentBackgroundIv)
        }

        googleMap = it

        googleMap.uiSettings.isZoomControlsEnabled = true

        setMapStyle(googleMap)

        googleMap.setOnMarkerClickListener { marker ->
            onMarkerClick(marker)
        }

        enableMyLocation()

    }

    private fun onMarkerClick(marker: Marker): Boolean {
        val clickCount = marker.tag as? Boolean

        val distanceLocation = Location("")

        distanceLocation.longitude = marker.position.longitude
        distanceLocation.latitude = marker.position.latitude

        val distance = BigDecimal(0.62*(lastLocation.distanceTo(distanceLocation) / 1000))
            .setScale(2, RoundingMode.HALF_EVEN)
        Timber.d("Distance is $distance")


        if (clickCount == false) {
            Timber.d("This marker coordinates: " + marker.snippet)

            lastMarker?.tag = false
            lastMarker?.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.small_hearthstone_logo))
            lastMarker = marker
            marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.hearthstone_logo))
            marker.tag = true
        }

        val modalSheetView = layoutInflater.inflate(R.layout.fragment_modal_bottom_sheet, null)
        modalSheetView.locationTitle.text = marker.title
        modalSheetView.locationAddress.text = marker.snippet
        modalSheetView.distanceTextView.text = getString(R.string.maps_dialog_distance, distance)
        modalSheetView.dialog_directions_button.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://www.google.com/maps/dir/?api=1&destination=${marker.snippet}"))
            startActivity(intent)
        }
        
        val dialog = BottomSheetDialog(this.requireContext())
        dialog.setContentView(modalSheetView)
        dialog.show()

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
            if (!success) {
                Timber.e("Style parsing failed.")
            }
        } catch (e: Resources.NotFoundException) {
            Timber.e(e, "Cant find style. Error: ")
        }
    }

    private fun getAddress(lat: LatLng): String? {
        val geocoder = Geocoder(this.requireContext())
        val list = geocoder.getFromLocation(lat.latitude, lat.longitude, 1)
        return list[0].getAddressLine(0)
    }

    private fun isPermissionGranted() : Boolean {
        return ContextCompat.checkSelfPermission(
            this.requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun enableMyLocation() {
        if (isPermissionGranted()) {
            if (ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this.requireContext(),
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Timber.d("Case test")
            }

            googleMap.isMyLocationEnabled = true

            fusedLocationClient.lastLocation.addOnSuccessListener(this.requireActivity()) { location ->

                if (location != null) {
                    lastLocation = location
                    currentLatLng = LatLng(location.latitude, location.longitude)
                    val locationString = "${location.latitude},${location.longitude}"
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
                    viewModel.getStores(locationString, 100000, "book_store")
                    Timber.d("Made the map")

                }
            }
        }
        else {
            Timber.d("Sad, no permissions")
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

}
