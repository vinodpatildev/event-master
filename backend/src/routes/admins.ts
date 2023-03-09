import express from "express";
import * as AdminController from "../controllers/admins";
import { requiresAuth } from "../middleware/auth";

const router = express.Router();

router.get("/",requiresAuth, AdminController.getAuthenticatedAdmin);

router.post("/signup",AdminController.signUp);

router.post("/login",AdminController.login);

router.post("/logout",AdminController.logout);

export default router;
