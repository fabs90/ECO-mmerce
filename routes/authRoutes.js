const express = require('express');
const { register, login, logout, googleAuth } = require('../controllers/authController');

const router = express.Router();

router.post('/register', register);
router.post('/login', login);
router.post('/logout', logout);
router.post('/google-auth', googleAuth);

module.exports = router;
