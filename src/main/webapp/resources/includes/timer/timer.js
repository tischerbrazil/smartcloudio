const countDownDate = new Date("May 22, 2023 8:00:00").getTime();
const daysElement = document.getElementById("days");
const hoursElement = document.getElementById("hours");
const minutesElement = document.getElementById("minutes");
const secondsElement = document.getElementById("seconds");
const dayLabel = document.getElementById("day-label");
const hourLabel = document.getElementById("hours-label");
const minutesLabel = document.getElementById("minutes-label");
const secondsLabel = document.getElementById("seconds-label");

const x = setInterval(function () {
  const now = new Date().getTime();
  let distance = countDownDate - now;
  if (distance < 0) {
    distance = countDownDate - distance;
  }

  const days = Math.floor(distance / (1000 * 60 * 60 * 24));
  const hours = Math.floor(
    (distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
  );
  const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((distance % (1000 * 60)) / 1000);

  daysElement.innerHTML = days;
  hoursElement.innerHTML = hours;
  minutesElement.innerHTML = minutes;
  secondsElement.innerHTML = seconds;
  dayLabel.innerHTML = "DIA";
  hourLabel.innerHTML = "HORAS";
  minutesLabel.innerHTML = "MIN.";
  secondsLabel.innerHTML = "SEG.";
}, 1000);


//Mobile
const countDownDatemobile = new Date("May 22, 2023 8:00:00").getTime();

const daysMobileElement = document.getElementById("days-mobile");
const hoursMobileElement = document.getElementById("hours-mobile");
const minutesMobileElement = document.getElementById("minutes-mobile");
const secondsMobileElement = document.getElementById("seconds-mobile");
const dayLabelMobile = document.getElementById("day-label-mobile");
const hourLabelMobile = document.getElementById("hours-label-mobile");
const minutesLabelMobile = document.getElementById("minutes-label-mobile");
const secondsLabelMobile = document.getElementById("seconds-label-mobile");

const xmobile = setInterval(function () {
  const now = new Date().getTime();
  let distance = countDownDatemobile - now;
  if (distance < 0) {
    distance = countDownDatemobile - distance;
  }

  const days = Math.floor(distance / (1000 * 60 * 60 * 24));
  const hours = Math.floor(
    (distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)
  );
  const minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
  const seconds = Math.floor((distance % (1000 * 60)) / 1000);

  daysMobileElement.innerHTML = days;
  hoursMobileElement.innerHTML = hours;
  minutesMobileElement.innerHTML = minutes;
  secondsMobileElement.innerHTML = seconds;
  dayLabelMobile.innerHTML = "DIAS";
  hourLabelMobile.innerHTML = "HORAS";
  minutesLabelMobile.innerHTML = "MIN.";
  secondsLabelMobile.innerHTML = "SEG.";
}, 1000);


