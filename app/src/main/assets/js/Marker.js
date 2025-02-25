
function ClearMarker(map, sourceMarker) {
    sourceMarker.clear();
    SelectMarkerPiont = null;
    map.render();
}

function Marker(map, sourceMarker, id, name, description, lon, lat, color, src, scale) {

    var MarkerPiont = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([lon, lat])),
        name: name
    });

    MarkerPiont.setId(id);
    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon({
            //color: color,
            src: src
        })
//        ,stroke: new ol.style.Stroke({
//            color: 'red',
//            width: 3
//        }),
//        fill: new ol.style.Fill({
//            color: 'rgba(255, 0, 0, 0.1)'
//        })
    });
    iconStyle.getImage().setScale(scale);

    MarkerPiont.setStyle(iconStyle);
    sourceMarker.addFeature(MarkerPiont);
    map.render();
/*
    //地理坐标
   var pos = ol.proj.fromLonLat([lon, lat]);

   //创建address
   var div = document.createElement("div");
   div.innerHTML = "<a class='address' id='" + id + "' title='Marker' target='_self' href='#'></a>";
   var Element= div.childNodes[0];
    //初始化文本覆盖层
    var text = new ol.Overlay({
        //位置
        position: pos,
        //覆盖层的元素
        element: Element //document.getElementById('address')
    });
    //将文本覆盖层添加到map中
    map.addOverlay(text);
    //设置文本覆盖层的内容为之前创建的覆盖层的title属性
    text.getElement().innerText = name;
    text.getElement().title = description; */
}

function CircleMarker(map, sourceMarker, lon, lat, radius, color) {
    //const circle4326 = circularPolygon([x, y], radius, 64);
    //const circle3857 = circle4326.clone().transform('EPSG:4326', 'EPSG:3857');
    //vectorLayer4326.getSource().addFeature(new Feature(circle4326));
    //vectorLayer3857.getSource().addFeature(new Feature(circle3857));


    //ol/geom/Polygon~circular
    //var circle3857 = ol.geom.Polygon.circularPolygon([lon, lat], radius, 64).transform('EPSG:4326', 'EPSG:3857');
    //sourceMarker.addFeature(new Feature(circle3857));

	var circle = new ol.geom.Circle([lon, lat], radius);
	circle = circle.clone().transform('EPSG:4326', 'EPSG:3857');;
	var precisionCircle = ol.geom.Polygon.circular(
    //new ol.Sphere(6378137),
    circle.getCenter(),
    circle.getRadius(),
    64);

    var PolygonCrircle=ol.geom.Polygon.fromCircle(circle);

    var circleFeature= new ol.Feature({ geometry: PolygonCrircle});

    var circleStyle= new ol.style.Style({
        stroke: new ol.style.Stroke({
            color: 'red',
            width: 2
        }),
        fill: new ol.style.Fill({
            color: 'rgba(255, 0, 0, 0)'
        })
    });

    circleFeature.setStyle(circleStyle);
    sourceMarker.addFeature(circleFeature);
    map.render();
}

var SelectMarkerPiont;

function SelectMarker(map, sourceMarker, id, name, description, lon, lat, color, src, scale) {

    if (SelectMarkerPiont != undefined)
    {
        sourceMarker.removeFeature(SelectMarkerPiont);
        //getFeatures().forEach(function(feature)
        //sourceMarker.removeFeature(sourceMarker.getFeatureById(MarkerPiont.getId()));
    }
    SelectMarkerPiont = new ol.Feature({
        geometry: new ol.geom.Point(ol.proj.fromLonLat([lon, lat])),
        name: name
    });
    if (id.indexOf('CurrSelect') < 0) {
        SelectMarkerPiont.setId(id + 'CurrSelect');
    }
    else {
        SelectMarkerPiont.setId(id);
    }
    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon({
            //color: color,
            src: 'images/alart-phone.png'
        })
        //        ,stroke: new ol.style.Stroke({
        //            color: 'red',
        //            width: 3
        //        }),
        //        fill: new ol.style.Fill({
        //            color: 'rgba(255, 0, 0, 0.1)'
        //        })
    });
    //iconStyle.getImage().setScale(scale);

    SelectMarkerPiont.setStyle(iconStyle);
    sourceMarker.addFeature(SelectMarkerPiont);
    map.render();
    /*
        //地理坐标
       var pos = ol.proj.fromLonLat([lon, lat]);

       //创建address
       var div = document.createElement("div");
       div.innerHTML = "<a class='address' id='" + id + "' title='Marker' target='_self' href='#'></a>";
       var Element= div.childNodes[0];
        //初始化文本覆盖层
        var text = new ol.Overlay({
            //位置
            position: pos,
            //覆盖层的元素
            element: Element //document.getElementById('address')
        });
        //将文本覆盖层添加到map中
        map.addOverlay(text);
        //设置文本覆盖层的内容为之前创建的覆盖层的title属性
        text.getElement().innerText = name;
        text.getElement().title = description; */
}
