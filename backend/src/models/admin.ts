import { InferSchemaType, model, Schema } from "mongoose";

const adminSchema = new Schema({
    username: { type : String, required: true, unique: true },
    email: { type : String, required: true, unique: true, select: false },
    password: { type : String, required: true, select: false },
    name:{type:String},
    otp:{ type:String, default:'', select:false },
    profile_image_url: {type:String},
    events_created: [
        {
          event: {
            type: Schema.Types.ObjectId,
            ref: "event",
            required: true,
          },
          present: { type: Boolean, default: false },
        },
      ],
});

adminSchema.set("toJSON", {
    transform: function (doc, ret) {
      delete ret.events_created;
      return ret;
    },
  });

type Admin = InferSchemaType<typeof adminSchema>;

export default model<Admin>("Admin", adminSchema);

