var lat;
var lon;

function trova(){



GrilloNav.trova();

lat = GrilloNav.getLat();
lon = GrilloNav.getLon();

document.getElementById("Lat").setAttribute("value", lat);
document.getElementById("Lon").setAttribute("value", lon);

}


function salva(){

GrilloNav.salva();

}


function salvaf(){

GrilloNav.salvafile(document.getElementById("name").value,document.getElementById("description").value,document.getElementById("Lat").value,document.getElementById("Lon").value);

}

function map(){

lat = document.getElementById("Lat").value ;
lon = document.getElementById("Lon").value;

GrilloNav.map();

}
var map ;
var kmlLayer ;
function initialize() {
  var myLatlng = new google.maps.LatLng(lat,lon );
  var mapOptions = {
    zoom: 12,
    center: myLatlng
  };

   map = new google.maps.Map(document.getElementById('map-canvas'),
      mapOptions);

  kmlLayer = new google.maps.KmlLayer({
    url: GrilloNav.getKmlFile(),
    suppressInfoWindows: true,
    map: map
  });

  google.maps.event.addListener(kmlLayer, 'click', function(kmlEvent) {
    var text = kmlEvent.featureData.description;
    showInContentWindow(text);
  });

  function showInContentWindow(text) {
    var sidediv = document.getElementById('content-window');
    sidediv.innerHTML = text;
  }
}


