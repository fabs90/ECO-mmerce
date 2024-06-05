const { db } = require('../config/firebaseConfig');

// Create a new data entry
const createData = async (req, res) => {
    const { userId, data } = req.body;

    try {
        const dataRef = db.collection('data').doc();
        await dataRef.set({ userId, ...data, createdAt: new Date() });

        return res.status(201).json({
            status: "successful",
            message: "Data created successfully",
            dataId: dataRef.id
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};

// Get data entries for a user
const getData = async (req, res) => {
    const { userId } = req.params;

    try {
        const dataSnapshot = await db.collection('data').where('userId', '==', userId).get();
        const data = dataSnapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));

        return res.status(200).json({
            status: "successful",
            data
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};

// Update a data entry
const updateData = async (req, res) => {
    const { dataId } = req.params;
    const { data } = req.body;

    try {
        const dataRef = db.collection('data').doc(dataId);
        await dataRef.update({ ...data, updatedAt: new Date() });

        return res.status(200).json({
            status: "successful",
            message: "Data updated successfully"
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};

// Delete a data entry
const deleteData = async (req, res) => {
    const { dataId } = req.params;

    try {
        const dataRef = db.collection('data').doc(dataId);
        await dataRef.delete();

        return res.status(200).json({
            status: "successful",
            message: "Data deleted successfully"
        });
    } catch (error) {
        console.error(error);
        return res.status(500).json({
            status: "error",
            message: "Internal Server Error"
        });
    }
};

module.exports = { createData, getData, updateData, deleteData };
