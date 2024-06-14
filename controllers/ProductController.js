const { db } = require('../config/firebaseConfig');
const jwt = require('jsonwebtoken');

const ProductController = {
    async get_products(req, res) {
        const qNew = req.query.new;
        const qCategory = req.query.category;
        try {
            let query = db.collection('products');
            if (qNew) {
                query = query.orderBy('createdAt', 'desc').limit(5);
            } else if (qCategory) {
                query = query.where('categories', 'array-contains', qCategory);
            }
            const snapshot = await query.get();
            const products = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
            return res.status(200).json({
                status: "successful",
                message: "Products fetched successfully",
                products
            });
        } catch (error) {
            console.error(error);
            return res.status(500).json({
                status: "error",
                message: "Internal Server Error"
            });
        }
    },

    async get_product(req, res) {
        try {
            const productDoc = await db.collection('products').doc(req.params.id).get();
            if (!productDoc.exists) {
                return res.status(404).json({
                    status: "fail",
                    message: "Product doesn't exist"
                });
            } else {
                return res.status(200).json({
                    status: "successful",
                    message: "Product fetched successfully",
                    product: { id: productDoc.id, ...productDoc.data() }
                });
            }
        } catch (error) {
            console.error(error);
            return res.status(500).json({
                status: "error",
                message: "Internal Server Error"
            });
        }
    },

    async create_product(req, res) {
        try {
            const newProduct = await db.collection('products').add({
                ...req.body,
                createdAt: new Date() 
            });
            return res.status(201).json({
                status: "successful",
                message: "Product created successfully",
                id: newProduct.id
            });
        } catch (error) {
            console.error(error);
            return res.status(500).json({
                status: "error",
                message: "Internal Server Error"
            });
        }
    },

    async update_product(req, res) {
        try {
            await db.collection('products').doc(req.params.id).set(req.body, { merge: true });
            return res.status(200).json({
                status: "successful",
                message: "Product updated successfully"
            });
        } catch (error) {
            console.error(error);
            return res.status(500).json({
                status: "error",
                message: "Internal Server Error"
            });
        }
    },

    async delete_product(req, res) {
        try {
            await db.collection('products').doc(req.params.id).delete();
            return res.status(200).json({
                status: "successful",
                message: "Product deleted successfully"
            });
        } catch (error) {
            console.error(error);
            return res.status(500).json({
                status: "error",
                message: "Internal Server Error"
            });
        }
    }
};

module.exports = ProductController;