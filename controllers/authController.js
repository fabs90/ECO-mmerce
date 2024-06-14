const { admin, db } = require('../config/firebaseConfig');
const bcrypt = require('bcrypt');
const jwt = require('jsonwebtoken');
const { v4: uuidv4 } = require('uuid');

// Helper functions
const hashPassword = async (password) => {
    const salt = await bcrypt.genSalt(10);
    return await bcrypt.hash(password, salt);
};

const comparePassword = async (plainPassword, hashedPassword) => {
    return await bcrypt.compare(plainPassword, hashedPassword);
};

const generateToken = (payload) => {
    return jwt.sign(payload, 'your_jwt_secret', { expiresIn: '1h' });
};

const register = async (req, res) => {
    const { name, email, phoneNumber, password } = req.body;

    try {
        const userRef = db.collection('users').doc(email);
        const userDoc = await userRef.get();

        if (userDoc.exists) {
            return res.status(400).json({
                status: "fail",
                message: "Failed to register account/Account already created"
            });
        }

        const userId = uuidv4();
        const hashedPassword = await bcrypt.hash(password, 10);
        
        await userRef.set({
            userId,
            name,
            email,
            phoneNumber,
            password: hashedPassword,
            createdAt: new Date()
        });

        return res.status(201).json({
            status: "successful",
            message: "User registered successfully",
            userId
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};

const login = async (req, res) => {
    const { email, password } = req.body;

    try {
        const userRef = db.collection('users').doc(email);
        const userDoc = await userRef.get();

        if (!userDoc.exists || !(await comparePassword(password, userDoc.data().password))) {
            return res.status(400).json({
                status: "fail",
                message: "Account not found"
            });
        }

        const token = generateToken({ email });

        await userRef.update({
            "token.auth": token,
            "updatedAt": new Date()
        });

        return res.status(200).json({
            status: "successful",
            message: "User login successful",
            token
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};

const logout = async (req, res) => {
    const { email } = req.body;

    try {
        const userRef = db.collection('users').doc(email);
        await userRef.update({
            "token.auth": null,
            "updatedAt": new Date()
        });

        return res.status(200).json({
            status: "successful",
            message: "User logout successful"
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};

const googleAuth = async (req, res) => {
    const { idToken } = req.body;

    try {
        // Verify the ID token using Firebase Admin SDK
        const decodedToken = await admin.auth().verifyIdToken(idToken);
        const { email, name, picture, uid } = decodedToken;

        // Check if user exists in Firestore
        const userRef = admin.firestore().collection('users').doc(uid);
        const doc = await userRef.get();

        if (!doc.exists) {
            // If user does not exist, create a new user
            await userRef.set({
                email,
                name,
                picture,
                createdAt: admin.firestore.FieldValue.serverTimestamp(),
            });
        }

        // Generate your own JWT token if needed
        const token = jwt.sign({ uid, email }, 'your_jwt_secret', { expiresIn: '1h' });

        return res.status(200).json({
            status: "successful",
            message: "User signed in successfully",
            token
        });
    } catch (error) {
        console.error('Error verifying ID token:', error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};


module.exports = { register, login, logout, googleAuth };