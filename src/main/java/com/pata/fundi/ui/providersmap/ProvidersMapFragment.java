package com.whebtos.e_chiro.ui.providersmap;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.esri.arcgisruntime.concurrent.ListenableFuture;
import com.esri.arcgisruntime.data.Feature;
import com.esri.arcgisruntime.data.ServiceFeatureTable;
import com.esri.arcgisruntime.geometry.Envelope;
import com.esri.arcgisruntime.geometry.SpatialReferences;
import com.esri.arcgisruntime.layers.FeatureLayer;
import com.esri.arcgisruntime.mapping.GeoElement;
import com.esri.arcgisruntime.mapping.view.Callout;
import com.esri.arcgisruntime.mapping.view.DefaultMapViewOnTouchListener;
import com.esri.arcgisruntime.mapping.view.IdentifyLayerResult;
import com.whebtos.e_chiro.BuildConfig;
import com.whebtos.e_chiro.R;

import com.esri.arcgisruntime.ArcGISRuntimeEnvironment;
import com.esri.arcgisruntime.mapping.ArcGISMap;
import com.esri.arcgisruntime.mapping.BasemapStyle;
import com.esri.arcgisruntime.mapping.Viewpoint;
import com.esri.arcgisruntime.mapping.view.MapView;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProvidersMapFragment  extends Fragment{

    private MapView mMapView;

    private Callout mCallout;


    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {




        View root = inflater.inflate(R.layout.fragment_providers_map, parent, false);

        // authentication with an API key or named user is required to access basemaps and other
        // location services
        ArcGISRuntimeEnvironment.setApiKey("AAPKe573eedf537e4e7087364c54b48cc48azoAVbma4GjtqLvJ_6DEU12JGkdXsKSwZjdEpzHFmWjQmMA11MGMuMYzmZIdmvQ9E");

        // inflate MapView from layout
        mMapView = root.findViewById(R.id.mapView);

        // create a map with the terrain with labels basemap
        ArcGISMap map = new ArcGISMap(BasemapStyle.ARCGIS_NAVIGATION);

        // create feature layer with its service feature table
        // create the service feature table
        ServiceFeatureTable serviceFeatureTable = new ServiceFeatureTable(
                getResources().getString(R.string.service_url));

        // create the feature layer using the service feature table
        FeatureLayer featureLayer = new FeatureLayer(serviceFeatureTable);

        // add the layer to the map
        map.getOperationalLayers().add(featureLayer);

        // set the map to be displayed in the mapview
        mMapView.setMap(map);

        // set an initial viewpoint
        mMapView.setViewpoint(new Viewpoint(new com.esri.arcgisruntime.geometry.Point( 36.816023, -1.298794, SpatialReferences.getWgs84()), 600000));

        mCallout = mMapView.getCallout();

        // set an on touch listener to listen for click events
        mMapView.setOnTouchListener(new DefaultMapViewOnTouchListener(root.getContext(), mMapView) {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                // remove any existing callouts
                if (mCallout.isShowing()) {
                    mCallout.dismiss();
                }
                // get the point that was clicked and convert it to a point in map coordinates
                final Point screenPoint = new Point(Math.round(e.getX()), Math.round(e.getY()));
                // create a selection tolerance
                int tolerance = 10;
                // use identifyLayerAsync to get tapped features
                final ListenableFuture<IdentifyLayerResult> identifyLayerResultListenableFuture = mMapView
                        .identifyLayerAsync(featureLayer, screenPoint, tolerance, false, 1);
                identifyLayerResultListenableFuture.addDoneListener(() -> {
                    try {
                        IdentifyLayerResult identifyLayerResult = identifyLayerResultListenableFuture.get();
                        // create a textview to display field values
                        TextView calloutContent = new TextView(root.getContext());
                        calloutContent.setTextColor(Color.BLACK);
                        calloutContent.setSingleLine(false);
                        calloutContent.setVerticalScrollBarEnabled(true);
                        calloutContent.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                        calloutContent.setMovementMethod(new ScrollingMovementMethod());
                        calloutContent.setLines(5);
                        for (GeoElement element : identifyLayerResult.getElements()) {
                            Feature feature = (Feature) element;
                            // create a map of all available attributes as name value pairs
                            Map<String, Object> attr = feature.getAttributes();
                            Set<String> keys = attr.keySet();
                            for (String key : keys) {
                                Object value = attr.get(key);
                                // format observed field value as date
                                if (value instanceof GregorianCalendar) {
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
                                    value = simpleDateFormat.format(((GregorianCalendar) value).getTime());
                                }
                                // append name value pairs to text view
                                calloutContent.append(key + " | " + value + "\n");
                            }
                            // center the mapview on selected feature
                            Envelope envelope = feature.getGeometry().getExtent();
                            mMapView.setViewpointGeometryAsync(envelope, 200);
                            // show callout
                            mCallout.setLocation(envelope.getCenter());
                            mCallout.setContent(calloutContent);
                            mCallout.show();
                        }
                    } catch (Exception e1) {
                        Log.e(getResources().getString(R.string.app_name), "Select feature failed: " + e1.getMessage());
                    }
                });
                return super.onSingleTapConfirmed(e);
            }
        });

        return root;
    }
}
