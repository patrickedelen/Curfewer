var mongoose = require('mongoose');

var parentSchema = new mongoose.Schema({
	_email        : {type: String, unique: true},
    _token        : {type: String},
	Home: {
		type       : {type: String},
		coordinates: {type: [Number]}    //[long,lat]
	},
    Kids: [{
        kidEmail: {type: String}
    }],
    Curfews: [{
        kidEmail: {type: String},
        date: {type: Date}
    }]
});

parentSchema.index({ Home: '2dsphere' });

module.exports = mongoose.model('Parent', parentSchema);