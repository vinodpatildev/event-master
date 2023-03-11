import { Button, Form, Modal } from "react-bootstrap";
import { useForm } from "react-hook-form";
import { Event } from "../models/event";
import { EventInput } from "../network/events_api";
import * as EventsApi from "../network/events_api";
import TextInputField from "./form/TextInputField";
import { useState } from "react";

interface AddEditEventDialogProps {
  eventToEdit?: Event;
  onDismiss: () => void;
  onEventSaved: (event: Event) => void;
}

const AddEditEventDialog = ({
  eventToEdit,
  onDismiss,
  onEventSaved,
}: AddEditEventDialogProps) => {
  const [startDate, setStartDate] = useState(new Date());
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<EventInput>({
    defaultValues: {
      title: eventToEdit?.title || "",
      department: eventToEdit?.department || "",
      organizer: eventToEdit?.organizer || "",
      description: eventToEdit?.description || "",
      type: eventToEdit?.type || "",
      event_link: eventToEdit?.event_link || "",
      start : eventToEdit?.start || new Date(),
      end : eventToEdit?.end || new Date(),
    },
  });

  async function onSubmit(input: EventInput) {
    try {
      let eventResponse: Event;
      if (eventToEdit) {
        eventResponse = await EventsApi.updateEvent(eventToEdit._id, input);
      } else {
        eventResponse = await EventsApi.createEvent(input);
      }
      onEventSaved(eventResponse);
    } catch (error) {
      console.error(error);
      alert(error);
    }
  }

  return (
    <Modal show onHide={onDismiss}>
      <Modal.Header closeButton>
        <Modal.Title>{eventToEdit ? "Edit Event" : "Add Event"}</Modal.Title>
      </Modal.Header>
      <Modal.Body>
        <Form id="addEditEventForm" onSubmit={handleSubmit(onSubmit)}>
          <TextInputField
            name="title"
            label="Title"
            type="text"
            placeholder="Expert session on Career Opportunities and Skill Development"
            register={register}
            registerOptions={{ required: "Required" }}
            error={errors.title}
          />
          <TextInputField
            name="department"
            label="Department"
            type="text"
            placeholder="Computer Department"
            register={register}
            registerOptions={{ required: "Required" }}
            error={errors.department}
          />
          <TextInputField
            name="organizer"
            label="Organizer"
            type="text"
            placeholder="Mr.Vinod Patil"
            register={register}
            registerOptions={{ required: "Required" }}
            error={errors.organizer}
          />
          <TextInputField
            name="description"
            label="Description"
            as="textarea"
            rows={5}
            placeholder="Expert session on Career Opportunities and Skill Development"
            error={errors.description}
            register={register}
          />
          <TextInputField
            name="type"
            label="Type"
            type="text"
            placeholder="Expert Session"
            register={register}
            registerOptions={{ required: "Required" }}
            error={errors.type}
          />
          <TextInputField
            name="event_link"
            label="Event Link"
            type="text"
            placeholder="https://raisoni.event.io/event_datails.php"
            register={register}
            registerOptions={{ required: "Required" }}
            error={errors.event_link}
          />
          <TextInputField
            name="start"
            label="Start Date"
            type="datetime-local"
            error={errors.start}
            register={register}
          />
          <TextInputField
            name="end"
            label="End Date"
            type="datetime-local"
            error={errors.start}
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
};

export default AddEditEventDialog;
