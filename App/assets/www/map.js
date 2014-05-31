function trova(){

var lat;
var lon;

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