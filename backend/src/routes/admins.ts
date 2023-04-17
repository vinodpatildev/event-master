import express from "express";
import * as AdminController from "../controllers/admins";
import { requiresAuth } from "../middleware/auth";

const router = express.Router();

router.get("/",requiresAuth, AdminController.getAuthenticatedAdmin);

router.post("/signup",AdminController.signUp);

router.post("/login",AdminController.login);

router.post("/logout",AdminController.logout);

router.post("/notify",AdminController.sendNotification);

router.post("/updateProfilePicture", AdminController.updateAdminProfilePicture);

router.post("/forget", AdminController.forgetAdminPassword);

router.post("/reset", AdminController.resetAdminPassword);


export default router;
