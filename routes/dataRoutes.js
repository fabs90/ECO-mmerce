const express = require('express');
const { getData } = require('../controllers/dataController');
const { authenticateToken } = require('../middleware/authMiddleware');

const router = express.Router();

router.get('/data', authenticateToken, getData);

module.exports = router;
