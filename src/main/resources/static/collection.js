const SERVER_URL = "http://localhost:8080"
const headers = {
  'Content-Type': 'application/json',
  // 'Access-Control-Allow-Origin': "localhost"
};

// json of data to capture
const deviceDetails = {
  userAgent: navigator.userAgent,
  networkDownLink: navigator.connection.downlink,
  networkType: navigator.connection.effectiveType,
  cookiesEnabled: navigator.cookieEnabled,
  doNotTrack: navigator.doNotTrack,
  language: navigator.language,
  locations: navigator.geolocation,
  isNewUser: window.sessionStorage.IsThisFirstTime_Log_From_LiveServer,
  referrer:  document.referrer,
  timeRegion: Intl.DateTimeFormat().resolvedOptions().timeZone,
  browserName: platform.name,
  browserVersion: platform.version,
  deviceScreenSize: window.screen.width + "x" + window.screen.height,
  deviceScreenWidth: window.screen.width,
  deviceScreenHeight: window.screen.height,
  deviceScreenOrientation: window.screen.orientation.type,
  deviceDescription: platform.description,
  deviceType: getDeviceType(),
  osName: platform.os.family.toUpperCase(),
  osVersion: platform.os.version,
}

// Get Browser details
document.getElementById("browser").textContent = deviceDetails.browserName;
document.getElementById("version").textContent = deviceDetails.browserVersion;
document.getElementById("layout").textContent = deviceDetails.deviceScreenSize;
document.getElementById("os").textContent = deviceDetails.osName;
document.getElementById("description").textContent = deviceDetails.deviceDescription;

// Get Screen details
document.getElementById("vertical").textContent = deviceDetails.deviceScreenWidth;
document.getElementById("horizontal").textContent = deviceDetails.deviceScreenHeight;
document.getElementById("ratio").textContent = deviceDetails.deviceScreenOrientation;

// Track events

var INITIAL_WAIT = 3000;
var INTERVAL_WAIT = 5000;
var ONE_SECOND = 1000;

var events = [
  "mouseup",
  "keydown",
  "scroll",
  "mousemove"
];
var startTime = Date.now();
var endTime = startTime + INITIAL_WAIT;

// var eventData.clickCount = 0;
// var eventData.buttonClicks = {
//   total: 0,
// };
// var eventData.linkClicks = { }
// var buttonClickCount = 0;
// var eventData.linkClickCount = 0;
// var eventData.keypressCount = 0;
// var eventData.scrollCount = 0;
// var eventData.mouseMovementCount = 0;

let eventData = {
  startTime: Date.now(),
  endTime: Date.now() + INITIAL_WAIT,
  activeTime: 0,
  idleTime: 0,
  clickLocations: [],
  buttonClicks: [],
  buttonClickCount: 0,
  linkClicks: [],
  linkClickCount: 0,
  scrollCount: 0,
  mouseMovementCount: 0,
  pageName: 'unidentified'
}

setInterval(function () {
  if (!document.hidden && startTime <= endTime) {
    startTime = Date.now();
    eventData.activeTime += ONE_SECOND;
    console.log(eventData.activeTime)
    document.getElementById("timer").innerHTML = formatTime(eventData.activeTime);

  }
}, ONE_SECOND);

document.addEventListener("DOMContentLoaded", function () {
  console.log("DOM fully loaded and parsed");
  checkCookie();
  checkSession();

  // if(!getCookie("deviceId")) {
    sendInitialData();
  // }

  document.getElementById("page").innerHTML = window.location.pathname;
  eventData.pageName = window.location.pathname

  events.forEach(function (e) {
    document.addEventListener(e, function () {
      endTime = Date.now() + INTERVAL_WAIT;
      if (e === "mouseup") {
        eventData.clickLocations.push({
          x: event.pageX,
          y: event.pageY
        })


        document.getElementById("click").innerHTML = JSON.stringify(eventData.clickLocations, null, 2);
        if (event.target.nodeName === 'BUTTON') {
          // if(!eventData.buttonClicks[event.target.innerText]){
          //   eventData.buttonClicks[event.target.innerText] = 0;
          // }
          // eventData.buttonClicks[event.target.innerText] += 1;
          eventData.buttonClicks.push({
              buttonName: event.target.innerText,
              clickTime: Date.now(),
          })
          eventData.buttonClickCount += 1;
          console.log('button clicks', eventData.buttonClicks);
          document.getElementById("button").innerHTML = JSON.stringify(eventData.buttonClicks, null, 2);
        } else if (event.target.nodeName === 'A') {
          console.log(event.target.innerText)
          eventData.linkClickCount++;
          // const linkClickCount = eventData.linkClicks[event.target.innerText] ? eventData.linkClicks[event.target.innerText].clickCount : 0

          // eventData.linkClicks[event.target.innerText] = {
          //   clickCount: linkClickCount + 1,
          //   linkDestination: event.target.href,
          //   clickTime: []
          // }

          eventData.linkClicks.push({
              linkName: event.target.innerText,
              linkDestination: event.target.href,
              clickTime: Date.now()
          })

          console.log('clicks: ', eventData.linkClicks);
          document.getElementById("link").innerHTML = eventData.linkClickCount;
          document.getElementById("linksClicked").innerHTML = JSON.stringify(eventData.linkClicks, null, 2);
        }
      }
      else if (e === "scroll") {
        eventData.scrollCount++;
        document.getElementById("scroll").innerHTML = eventData.scrollCount;
      }
      else if (e === "mousemove") {
        eventData.mouseMovementCount++;
        document.getElementById("mouse").innerHTML = eventData.mouseMovementCount;
      }
    });
  });
});

document.addEventListener('visibilitychange', function logData() {
  if (document.visibilityState === 'hidden') {
    console.log('leaving page')
    eventData.endTime = Date.now()

    eventData.idleTime = eventData.endTime - eventData.startTime - eventData.activeTime

    document.getElementById("idle").innerHTML = formatTime(eventData.idleTime);
    console.log('idle time', eventData.idleTime, 'date now', eventData.endTime)
    // eventData.pageName = doc
    console.log("page: ", eventData.pageName)

    const sessionId = sessionStorage.getItem("sessionId");
    const deviceId = getCookie("deviceId");
    const blob = new Blob(
      [JSON.stringify({
        ...eventData,
        clientSessionId: sessionId,
        deviceId
      })],
      {type: 'application/json'}
    )

    navigator.sendBeacon(`${SERVER_URL}/capture/page-event`, blob);
    eventData.buttonClicks = []
    eventData.linkClicks = []
    eventData.clickLocations = []
  }
});

function getDeviceType() {
  return /iPad/.test(navigator.userAgent) ? "IPAD"
    : /iPhone/.test(navigator.userAgent) ? "IPHONE"
    : /Android/.test(navigator.userAgent) ? "ANDROID"
    : /Windows Phone/.test(navigator.userAgent) ? "WINDOWS_PHONE"
    : "DESKTOP"
}

function getOSName() {
  var OSName = "UNIDENTIFIED";
  if (navigator.appVersion.indexOf("Win") != -1) OSName = "WINDOWS";
  if (navigator.appVersion.indexOf("Mac") != -1) OSName = "MACOS";
  if (navigator.appVersion.indexOf("X11") != -1) OSName = "UNIX";
  if (navigator.appVersion.indexOf("Linux") != -1) OSName = "LINUX";
  if (navigator.appVersion.indexOf("Android") != -1) OSName = "ANDROID";
  if (navigator.appVersion.indexOf("Ios") != -1) OSName = "IOS";

  return OSName
}

function formatTime(ms) {
  return Math.floor(ms / 1000);
}

// Cookie
function setCookie(cookieName, cookieValue) {
    const expires = new Date(2147483647 * 1000).toUTCString();
    document.cookie = cookieName + "=" + cookieValue + ";" + expires + ";";
}

function getCookie(cookieName) {
    var name = cookieName + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var cookieArray = decodedCookie.split(";");
    for (var i = 0; i < cookieArray.length; i++) {
        var cookie = cookieArray[i];
        while (cookie.charAt(0) == " ") {
            cookie = cookie.substring(1);
        }
        if (cookie.indexOf(name) == 0) {
            return cookie.substring(name.length, cookie.length);
        }
    }
    return "";
}

function checkCookie() {
    const deviceId = getCookie("deviceId");

    // console.log('deviceId:', deviceId)

    if (deviceId === null || deviceId === "") {
      // const uniqueId = "id-" + (new Date()).getTime() + "-" + Math.random().toString(16).slice(2);
      // const uniqueId = makeUID();
      // f8fb04b3-3cfb-42de-8da5-e0a1e865ddb8
      console.log('null device ID cookie')
      const uniqueId = crypto.randomUUID();
      console.log("uniqueId: ", uniqueId)

      // setCookie("deviceId", uniqueId);
      // alert("You have no biscuit so go get one");
    }

    if (deviceId != "" && deviceId != null) {
        console.log("Welcome again " + deviceId);
    }
  }

function checkSession() {
  const sessionId = sessionStorage.getItem("sessionId");

  if (sessionId === null || sessionId === "") {
    const uniqueId = crypto.randomUUID();

    sessionStorage.setItem("sessionId", uniqueId);

  }

  if (sessionId != "" && sessionId != null) {
      console.log("Welcome back to session " + sessionId);
  }

  return sessionId
}

function makeUID() {
  return Date.now().toString(16) + "-" + Math.random().toString(16).slice(2);
}

function sendInitialData() {
  const hitUrl = `${SERVER_URL}/capture/hit`
  const deviceId = getCookie("deviceId");
  let sessionId = sessionStorage.getItem("sessionId");
  if (sessionId === null) {
    sessionId = checkSession()
  }
  const data = {...deviceDetails, deviceId, clientSessionId: sessionId}
  console.log('sending first data: of', deviceId)
  console.log('data', data)
  const body = JSON.stringify(data)

  fetch(hitUrl, {
    method: 'POST',
    headers: new Headers(headers),
    credentials: "same-origin",
    // mode: "no-cors",
    body: JSON.stringify(data)
  })
  .then(response => response.json())
  .then(response => {
    console.log('request sent with response: ', response)
    if (response && response.message === 'Success') {
      console.log('response', response)
      setCookie("deviceId", response.data);
      console.log('updated cookie to', getCookie("deviceId"))
    }
  }).catch(e => console.error(e))
}

// 44bfb269-e7b3-495c-b19a-e9c1bace26c5