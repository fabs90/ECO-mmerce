const express = require('express');
const router = express.Router();

const ProductController = require('../controllers/ProductController'); 

const { authenticateToken } = require('../middleware/authMiddleware');

router.get('/', ProductController.get_products);
router.get('/:id', ProductController.get_product);
router.post('/',authenticateToken , ProductController.create_product);
router.put('/:id', authenticateToken, ProductController.update_product);
router.delete('/:id', authenticateToken, ProductController.delete_product);

module.exports = router;