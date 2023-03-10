import { useForm } from "react-hook-form";
import { Admin } from "../models/admin";
import { LoginCredentials } from "../network/events_api";
import * as EventsApi from "../network/events_api";
import { Alert, Button, Form, Modal } from "react-bootstrap";
import  TextInputField  from "./form/TextInputField";
import styleUtils from "../styles/utils.module.css";
import {useState} from "react";
import { UnauthorizedError } from "../errors/http_errors";

interface LoginModalProps{
    onDismiss: () => void,
    onLoginSuccessful: (admin:Admin) => void,
}

const LoginModal = ({onDismiss, onLoginSuccessful}: LoginModalProps) => {

    const [errorText, setErrorText] = useState<string|null>(null);

    const { register, handleSubmit, formState: {errors, isSubmitting} } = useForm<LoginCredentials>();

    async function onSubmit(credentials: LoginCredentials) {
        try {
            const newAdmin = await EventsApi.login(credentials);
            onLoginSuccessful(newAdmin);
        } catch(error){
            if(error instanceof UnauthorizedError){
                setErrorText(error.message);
            }else{
                alert(error);
            }
            console.error(error);
        }
    }

    return (
        <Modal show onHide={onDismiss}>
                <Modal.Header closeButton>
                    <Modal.Title>
                        Login
                    </Modal.Title>
                </Modal.Header>
                <Modal.Body>
                    {errorText &&
                        <Alert variant="danger">
                            {errorText}
                        </Alert>
                    }
                    <Form onSubmit={handleSubmit(onSubmit)}>
                        <TextInputField
                            name="username"
                            label="Username"
                            type="text"
                            placeholder="Username"
                            register={register}
                            registerOptions={{ required:"Required" }}
                            error={errors.username}
                        />
                        <TextInputField
                            name="password"
                            label="Password"
                            type="password"
                            placeholder="Password"
                            register={register}
                            registerOptions={{ required:"Required" }}
                            error={errors.password}
                        />
                        <Button
                            type="submit"
                            disabled={isSubmitting}
                            className={`btn btn-secondary ${styleUtils.width100}`}
                        >
                            Login
                        </Button>
                    </Form>
                </Modal.Body>
        </Modal>
    );
}

export default LoginModal;