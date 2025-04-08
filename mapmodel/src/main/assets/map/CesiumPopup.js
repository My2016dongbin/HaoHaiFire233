// cesium自定义弹框
function CesiumPopup() {
  if (!viewer) return
  const $L = {
    popup: {
      show: show,
      close: hide
    }
  }
  viewer.haohai = $L

  const handler3D = new Cesium.ScreenSpaceEventHandler(viewer.scene.canvas)
  handler3D.setInputAction(function(movement) {
    let pick = viewer.scene.pick(movement.position)
    // entity
    if (Cesium.defined(pick) && pick.id) {
      // show
      if (typeof pick.id === 'string') {
        // primitive
        let primitive = pick.primitive
        if (primitive.click instanceof Function) {
          // call click function
          primitive.click(primitive)
        }
        primitive = null
        pick = null
      } else if (typeof pick.id === 'object') {
        // entity
        let entity = pick.id
        if (entity.click instanceof Function) {
          // call click function
          entity.click(entity)
        }
        entity = null
        pick = null
      }
    } else {
      $('#trackPopUp').hide()
    }
  }, Cesium.ScreenSpaceEventType.LEFT_CLICK)
}
function show(entity, position) {
  // console.log(entity, position)
  const destination = position
  var pick = Cesium.SceneTransforms.wgs84ToWindowCoordinates(viewer.scene, position)
  if (entity.customizePopup) {
    const content = entity.customizePopup.html // 此处从entity中获取html数据，也可以以任意想要展示的内容填充
    const anchor = entity.customizePopup.anchor
    const obj = { position: pick, destination: destination, content: content, anchor: anchor }
    infoWindow(obj)
  }
}
function hide() {
  // $('.leaflet-popup-close-button').click()
  $('#trackPopUp').hide()
}
function positionPopUp(c, anchor) {
  let ax = 0
  let ay = 0
  if (anchor && anchor.length === 2) {
    ax = anchor[0]
    ay = anchor[1]
  }
  const x = c.x + ax
  const y = c.y + ay
  $('#trackPopUpContent').css('transform', 'translate3d(' + x + 'px, ' + y + 'px, 0)')
}
let removeHandler
function infoWindow(obj) {
  if (removeHandler) removeHandler.call()
  const picked = viewer.scene.pick(obj.position)
  if (Cesium.defined(picked)) {
    const pri = picked.primitive

    $('.cesium-selection-wrapper').show()
    $('#trackPopUpLink').empty()
    $('#trackPopUpLink').append(obj.content)
    let c = new Cesium.Cartesian2(obj.position.x, obj.position.y)
    $('#trackPopUp').show()
    positionPopUp(c, obj.anchor)
    removeHandler = viewer.scene.postRender.addEventListener(function() {
      let changedC
      if (pri._polyline != null) {
        const pos = {}
        pos.x = (pri._polyline._positions._value['0'].x + pri._polyline._positions._value[1].x) / 2
        pos.y = (pri._polyline._positions._value['0'].y + pri._polyline._positions._value[1].y) / 2
        pos.z = (pri._polyline._positions._value['0'].z + pri._polyline._positions._value[1].z) / 2
        changedC = Cesium.SceneTransforms.wgs84ToWindowCoordinates(viewer.scene, pos)
      } else {
        const pos = {}
        pos.x = pri.position.x
        pos.y = pri.position.y
        pos.z = pri.position.z
        changedC = Cesium.SceneTransforms.wgs84ToWindowCoordinates(viewer.scene, pos)
      }
      // If things moved, move the popUp too
      if ((c.x !== changedC.x) || (c.y !== changedC.y)) {
        positionPopUp(changedC, obj.anchor)
        c = changedC
      }
    })
    $('.leaflet-popup-close-button').click(function() {
      $('#trackPopUp').hide()
      $('#trackPopUpLink').empty()
      $('.cesium-selection-wrapper').hide()
      removeHandler.call()
      return false
    })
    // weather
    $('.forewarn-popup-weather-close-btn').click(function() {
      $('.forewarn-popup-content-weather').hide()
    })
    return pri
  }
}
