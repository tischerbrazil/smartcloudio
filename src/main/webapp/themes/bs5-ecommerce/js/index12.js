

document.addEventListener("DOMContentLoaded", function()
{
  document.querySelector(".spinner-wrapper").style.opacity= "0";
  setTimeout(function(){
    document.querySelector(".spinner-wrapper").style.display = "none";
  }, 2000); 
});



setTimeout(function () {
$("#modal2").modal("show");
}, 3500);

function closeItem() {
 // $("#modal2").modal("hide");
}













//Fixed Navbar
var navbar1 = document.querySelector("#navbar1");
//var navbar2 = document.querySelector("#navbar2");
var sticky = navbar1.offsetTop;

window.onscroll = function () {
  if (window.pageYOffset >= sticky) {
    navbar1.classList.add("fixed");
    navbar1.classList.add("fixed2");
  } else {
    navbar1.classList.remove("fixed");
    navbar1.classList.remove("fixed2");
   // navbar2.classList.remove("fixed");
   // navbar2.classList.remove("color");
  }
};


