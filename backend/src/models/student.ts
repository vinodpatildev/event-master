import { InferSchemaType, model, Schema } from "mongoose";

const studentSchema = new Schema({
    username: { type : String, required: true, unique: true },
    email: { type : String, required: true, unique: true },
    password: { type : String, required: true, select: false },
    registration_no: { type: String },
    name: { type: String },
    dob: { type: String },
    mobile: {type: String },
    year: {type:String },
    department: {type: String },
    division: {type: String },
    passing_year: {type: String },
});

type Student = InferSchemaType<typeof studentSchema>;

export default model<Student>("Student", studentSchema);

