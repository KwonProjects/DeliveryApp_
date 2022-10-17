package co.kr.kwon.deliveryapp.widget.dialog

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.kr.kwon.deliveryapp.R
import co.kr.kwon.deliveryapp.data.entity.LocationLatLngEntity
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import org.koin.android.viewmodel.ext.android.viewModel

class BottomSheetFragment: BottomSheetDialogFragment(), OnMapReadyCallback {

    lateinit var mapFragment:SupportMapFragment

    lateinit var googleMap:GoogleMap

    lateinit var title: String

    lateinit var latLng : LatLng


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?)
    : View? {

         var map : View = inflater.inflate(R.layout.bottom_sheet_location,container,false)

         mapFragment =
             childFragmentManager?.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync(this)

        return map
    }

    override fun onMapReady(map: GoogleMap) {

        googleMap = map
        googleMap.addMarker(MarkerOptions().position(latLng).title(title))
        googleMap.maxZoomLevel
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15.0f))

    }

     fun setLatLonTitle(latLng: LatLng,title: String){
        this.latLng = latLng
         this.title = title
    }

}