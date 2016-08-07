var mongoose = require('mongoose');

var kidSchema = new mongoose.Schema({
	_email        : {type: String, unique: true},
    _token        : {type: String},
    _parentEmail  : {type: String},
    accepted      : {type: Boolean},
    Curfews: [{
        kidEmail: {type: String},
        date: {type: Date}
    }],
    Home: [{
		type       : {type: String},
		coordinates: {type: [Number]}    //[long,lat]
	}],
});


module.exports = mongoose.model('Kid', kidSchema);