function trova(){

var lat;
var lon;

GrilloNav.trova(lat, lon);

document.getElementById("Lat").setAttribute("value", lat);
document.getElementById("Lon").setAttribute("value", lon);

}


function salva(){

GrilloNav.salva();

}