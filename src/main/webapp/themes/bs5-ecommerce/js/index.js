


document.addEventListener("DOMContentLoaded", function()
{
  document.querySelector(".spinner-wrapper").style.opacity= "0";
  setTimeout(function(){
    document.querySelector(".spinner-wrapper").style.display = "none";
  }, 2000); 
});

//Menu-Modal-Dropdown
function toggleDropdownMenu() {
  var menu = document.getElementById("dropdown-menu");
  if (menu.style.display === "block") {
    menu.style.display = "none";
  } else {
    menu.style.display = "block";
  }
}

function toggleDropdownMenu2() {
  var menu = document.getElementById("dropdown-menu2");
  if (menu.style.display === "block") {
    menu.style.display = "none";
  } else {
    menu.style.display = "block";
  }
}

function toggleDropdownMenu3() {
  var menu = document.getElementById("dropdown-menu3");
  if (menu.style.display === "block") {
    menu.style.display = "none";
  } else {
    menu.style.display = "block";
  }
}

function toggleDropdownMenu4() {
  var menu = document.getElementById("dropdown-menu4");
  if (menu.style.display === "block") {
    menu.style.display = "none";
  } else {
    menu.style.display = "block";
  }
}

setTimeout(function () {
$("#modal2").modal("show");
}, 3500);

function closeItem() {
  $("#modal2").modal("hide");
}



// redirect index
const returnbtn = document.querySelector(".col-md-2");
function redirectBtnindex() {
  window.location.href = "index.html";
}

// redirect login e produto
const actionbtn = document.querySelector(
  ".text",
  ".link",
  "#iconsperson",
  ".account-login"
);
function redirectBtnlogin() {
  window.location.href = "Login.html";
}

const actionproductcard = document.querySelector(
  ".card-banner",
  ".card-price",
  ".conditions-price",
  ".conditions-payment",
  "title-text"
);
function redirectProductpage() {
  window.location.href = "produto.html";
}

const actionCheckout = document.querySelector(".iconscart");
function redirectIconCheckout() {
  window.location.href = "Cart.html";
}





//Fixed Navbar
var navbar1 = document.querySelector("#navbar1");
var navbar2 = document.querySelector("#navbar2");
var sticky = navbar1.offsetTop;

window.onscroll = function () {
  if (window.pageYOffset >= sticky) {
    navbar1.classList.add("fixed");
    navbar2.classList.add("fixed");
    navbar2.classList.add("color");
  } else {
    navbar1.classList.remove("fixed");
    navbar2.classList.remove("fixed");
    navbar2.classList.remove("color");
  }
};

