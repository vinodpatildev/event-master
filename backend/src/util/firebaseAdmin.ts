import * as admin from 'firebase-admin';

const serviceAccount = require('../../event-master-fcm-firebase-adminsdk-h9088-c6645f786c.json');

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

export default admin;