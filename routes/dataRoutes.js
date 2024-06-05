const express = require('express');
const { createData, getData, updateData, deleteData } = require('../controllers/dataController');

const router = express.Router();

router.post('/data', createData);
router.get('/data/:userId', getData);
router.put('/data/:dataId', updateData);
router.delete('/data/:dataId', deleteData);

module.exports = router;
