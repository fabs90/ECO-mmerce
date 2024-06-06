const { db } = require('../config/firebaseConfig');

const getData = async (req, res) => {
    try {
        const userRef = db.collection('users').doc(req.user.email);
        const userDoc = await userRef.get();

        if (!userDoc.exists) {
            return res.status(404).json({
                status: "fail",
                message: "User data not found"
            });
        }

        return res.status(200).json({
            status: "successful",
            data: userDoc.data()
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};

module.exports = { getData };
