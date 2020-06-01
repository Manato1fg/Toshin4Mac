/*
 * Author  : Manato0x2cc
 * License : Apache
 * Copyright (c) 2017 Manato Yo
 * http://www.apache.org/licenses/LICENSE-2.0
**/

/**
 * Notificate.js sends notificatation which appears some seconds where you want to appear.
 */

/**
 * @param message: The message is notificated
 *           x   : The x fixed
 *           y   : The y fixed
 */
function notificate(message, x, y, time = 2000) {
    var div = document.createElement("div");
    div.style.width = "auto";
    div.style.height = "auto";
    div.style.borderRadius = "4px";
    div.style.background = "rgba(50,50,50,0.5)";
    div.style.color = "#fff";
    div.style.position = "fixed";
    div.style.left = x + "px"
    div.style.top = y + "px";
    div.style.padding = "20px";

    div.innerHTML = message;
    document.body.appendChild(div);

    setTimeout(function () {
        document.body.removeChild(div);
    }, time);
}