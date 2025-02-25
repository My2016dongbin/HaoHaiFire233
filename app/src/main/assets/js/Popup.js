var container;
var content;
var closer;
var popTitle;


/** 
* Create an overlay to anchor the popup to the map. 
*/
var overlay;

//构建popup
function buildPopup() {
    container = document.getElementById('popup');
    content = document.getElementById('popup-content');
    closer = document.getElementById('popup-closer');
    /** 
    * Add a click handler to hide the popup. 
    * @return {boolean} Don't follow the href. 
    */
    closer.onclick = function () {
        overlay.setPosition(undefined);
        closer.blur();
        return false;
    };

    overlay = new ol.Overlay(/** @type {olx.OverlayOptions} */({
        element: container,
        autoPan: true,
        autoPanAnimation: {
            duration: 250   //当Popup超出地图边界时，为了Popup全部可见，地图移动的速度. 单位为毫秒（ms）  
        }
    }));
}