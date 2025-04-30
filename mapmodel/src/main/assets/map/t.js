function zoom({lat, lot}){
    this.lat = lat;
    this.lot = lot;
    return this
}
zoom.prototype.animate = function() {
    return this.lat + this.lot
}

const z = new zoom({lat:1, lot: 2})
console.log(z.animate())
const s = zoom({lat:1, lot: 2})
console.log(s)
// console.log(s.animate())