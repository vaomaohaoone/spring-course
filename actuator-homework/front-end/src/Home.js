import React, {Component} from 'react';
import './App.css';
import {Button, FormGroup, Link} from '@material-ui/core';
import {Form} from "reactstrap";
import BottomNavigation from '@material-ui/core/BottomNavigation';
import BottomNavigationAction from '@material-ui/core/BottomNavigationAction';
import ExitToAppIcon from '@material-ui/icons/ExitToApp';
import Grid from "@material-ui/core/Grid";

class Home extends Component {

    constructor(props) {
        super(props);
        this.state={loggedOut: false}
        this.onLogout = this.onLogout.bind(this);
    }

    onLogout = async () => {
        await (await fetch('logout').then(() => {
            localStorage.setItem("authenticated", "false")
            this.setState({loggedOut: true})
        }))
    }

    render() {
        if (localStorage.getItem("authenticated") != null && localStorage.getItem("authenticated") === "true" && this.state.loggedOut !== true ) {
            return (
                <div>
                    <Grid container alignItems="flex-start" justify="flex-end" direction="row">
                        <BottomNavigation showLabels={true}>
                            <BottomNavigationAction component={Button} label={"Выход"} value={"Выход"}
                                                    icon={<ExitToAppIcon/>} onClick={this.onLogout}/>
                        </BottomNavigation>
                    </Grid>

                    <Grid container xs>
                        <Form>
                            <FormGroup>
                                <Button variant="contained" href="/books" renderAs={Link} color="primary">Книги</Button>
                                <Button variant="contained" href="/authors" renderAs={Link}
                                        color="primary">Авторы</Button>
                                <Button variant="contained" href="/styles" renderAs={Link}
                                        color="primary">Жанры</Button>
                            </FormGroup>
                        </Form>
                    </Grid>
                </div>
            );
        } else
            return (
                <div>
                    <Grid container alignItems="flex-start" justify="flex-end" direction="row">
                        <BottomNavigation showLabels={true}>
                            <BottomNavigationAction component={Button} label={"Вход"} value={"Вход"}
                                                    icon={<ExitToAppIcon/>} href={"/login"}/>
                        </BottomNavigation>
                    </Grid>
                </div>
            );
    }
}

export default Home;