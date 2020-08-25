import React, {Component} from 'react';
import './App.css';
import {Button, Container, FormGroup, Link} from '@material-ui/core';
import {Form} from "reactstrap";

class Home extends Component {
    render() {
        return (
            <div>
                <Container fixed={true}>
                    <Form>
                        <FormGroup>
                            <Button variant="contained" href="/books" renderAs={Link} color="primary">Книги</Button>
                            <Button variant="contained" href="/authors" renderAs={Link} color="primary">Авторы</Button>
                            <Button variant="contained" href="/styles" renderAs={Link} color="primary">Жанры</Button>
                        </FormGroup>
                    </Form>
                </Container>
            </div>
        );
    }
}

export default Home;