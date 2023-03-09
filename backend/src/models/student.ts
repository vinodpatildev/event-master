import { InferSchemaType, model, Schema } from "mongoose";

const studentSchema = new Schema({
    username: { type : String, required: true, unique: true },
    email: { type : String, required: true, unique: true, select: false },
    password: { type : String, required: true, select: false }
});

type Student = InferSchemaType<typeof studentSchema>;

export default model<Student>("Student", studentSchema);

