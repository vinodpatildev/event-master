import { getAuthenticatedStudent } from './../controllers/students';
import express from "express";
import * as StudentController from "../controllers/students";
import { requiresAuth } from "../middleware/auth";

const router = express.Router();

router.get("/",requiresAuth, StudentController.getAuthenticatedStudent);

router.post("/signup", StudentController.signUp);

router.post("/login", StudentController.login);

router.post("/logout", StudentController.logout);

export default router;
