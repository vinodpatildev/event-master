import { Button, Form, Modal } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { Event } from "../models/event";
import { EventInput } from "../network/events_api";
import * as EventsApi from "../network/events_api";
import TextInputField from "./form/TextInputField";

interface AddEditEventDialogProps {
    eventToEdit?: Event,
    onDismiss: () => void,
    onEventSaved: (event:Event) => void
}

const AddEditEventDialog = ( {eventToEdit, onDismiss, onEventSaved}:AddEditEventDialogProps) => {

    const { register, handleSubmit, formState: { errors, isSubmitting } } = useForm<EventInput>({
        defaultValues: {
            title: eventToEdit?.title || "",
            text: eventToEdit?.text || "",
        }
    });

    async function onSubmit(input: EventInput){
        try{
            let eventResponse:Event;
            if(eventToEdit){
                eventResponse = await EventsApi.updateEvent(eventToEdit._id,input);
            }else{
                eventResponse = await EventsApi.createEvent(input);
            }
            onEventSaved(eventResponse);
        }
        catch(error){
            console.error(error);
            alert(error);
        }
    }

    return (
        <Modal show onHide={onDismiss}>
            <Modal.Header closeButton>
                <Modal.Title>
                    {eventToEdit ? "Edit Event" : "Add Event"}
                </Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <Form id="addEditEventForm"  onSubmit={handleSubmit(onSubmit)}>
                    <TextInputField 
                        name="title"
                        label="Title"
                        type="text"
                        placeholder="Title"
                        register={register}
                        registerOptions={{ required:"Required" }}
                        error={errors.title}
                    />
                    <TextInputField
                        name="text"
                        label="Text"
                        as="textarea"
                        rows={5}
                        placeholder="Text"
                        register={register}
                    />
                </Form>
            </Modal.Body>
            <Modal.Footer>
                <Button
                    type="submit"
                    form="addEditEventForm"
                    disabled={isSubmitting}
                    className="btn btn-secondary"
                >
                    Save
                </Button>
            </Modal.Footer>
        </Modal>
    );
}

export default AddEditEventDialog;