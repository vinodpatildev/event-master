import { InferSchemaType, model, Schema } from "mongoose";

const adminSchema = new Schema({
    username: { type : String, required: true, unique: true },
    email: { type : String, required: true, unique: true, select: false },
    password: { type : String, required: true, select: false }
});

type Admin = InferSchemaType<typeof adminSchema>;

export default model<Admin>("Admin", adminSchema);

