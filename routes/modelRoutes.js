const express = require('express');
const router = express.Router();
const modelController = require('../controllers/modelController'); 

router.get('/get-data', modelController.getData);

module.exports = router;
