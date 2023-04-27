import { getAuthenticatedStudent } from './../controllers/students';
import express from "express";
import * as StudentController from "../controllers/students";
import { requiresAuth } from "../middleware/auth";

const router = express.Router();

router.get("/",requiresAuth, StudentController.getAuthenticatedStudent);

router.post("/signup", StudentController.signUp);

router.post("/login", StudentController.login);

router.post("/logout", StudentController.logout);

router.post("/update/:studentId", StudentController.updateStudent);

router.post("/password/:studentId", StudentController.updateStudentPassword);

router.post("/forget", StudentController.forgetStudentPassword);

router.post("/reset", StudentController.resetStudentPassword);

router.post("/register", StudentController.registerStudentForEvent);

router.post("/mark", StudentController.markAttendanceStudentForEvent);

router.post("/updateProfilePicture", StudentController.updateStudentProfilePicture);


export default router;
