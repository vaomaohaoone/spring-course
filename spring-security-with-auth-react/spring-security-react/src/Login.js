import React, {Component} from "react";
import {Button, Container, FormGroup, Input} from '@material-ui/core';
import {Form} from "reactstrap";
import {Redirect} from "react-router-dom";
import Alert from '@material-ui/lab/Alert';
import InputLabel from "@material-ui/core/InputLabel";

class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {username: '', password: '', isAuthenticated: false, loginError: false}
        localStorage.setItem("authenticated", "false")
    }

    handleChange = (event) => {
        event.preventDefault();
        this.setState({[event.target.name]: event.target.value});
    }

    handleErrors(response) {
        if (!response.ok) throw new Error(response.status);
        return response;
    }

    login = async (event) => {
        event.preventDefault()
        const {username, password} = this.state;
        await (await fetch('auth', {
                headers: {"Authorization": "Basic " + window.btoa(username + ":" + password)}
            })
                .then(this.handleErrors)
                .then(() => {
                    localStorage.setItem("authenticated", "true")
                    this.setState({isAuthenticated: true, loginError: false})
                })
                .catch(error => {
                        localStorage.setItem("authenticated", "false");
                        this.setState({isAuthenticated: false, loginError: true})
                    }
                )
        )
    };

    render() {
        if (this.state.isAuthenticated === true) {
            return (<Redirect to={'/'}/>)
        } else {
            return (
                <div id="login">
                    <Form onSubmit={this.login}>
                        <Container maxWidth={"xs"} s>
                            {this.state.loginError ? (
                                <Alert severity="error">
                                    Ошибка при входе! Пожалуйста, проверьте свои учетные данные
                                    и попробуйте еще раз.
                                </Alert>
                            ) : null}
                            <FormGroup>
                                <InputLabel htmlFor="username">Имя пользователя</InputLabel>
                                <Input id="username" type="text" name="username" onChange={this.handleChange} placeholder="например admin"/>
                            </FormGroup>
                            <FormGroup>
                                <InputLabel htmlFor="password">Пароль</InputLabel>
                                <Input type="password" name="password" onChange={this.handleChange}
                                       placeholder="например admin"/>
                            </FormGroup>
                            <FormGroup>
                                <Button color="primary" variant="contained" type={"submit"} name="submit"
                                        value="Login">Войти</Button>
                            </FormGroup>
                        </Container>
                    </Form>
                </div>
            );
        }
    }
}

export default Login;