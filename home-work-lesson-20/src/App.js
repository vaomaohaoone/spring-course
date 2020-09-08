import React, {Component} from 'react'
import Home from "./Home";
import {BrowserRouter as Router} from "react-router-dom";
import Switch from "react-router-dom/es/Switch";
import Route from "react-router-dom/es/Route";
import BookList from "./BookList";
import BookEdit from "./BookEdit";
import AuthorList from "./AuthorList";
import AuthorEdit from "./AuthorEdit";
import StyleList from "./StyleList";
import StyleEdit from "./StyleEdit";
import Comments from "./Comments";

class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route path='/' exact={true} component={Home}/>
                    <Route path='/books' exact={true} component={BookList}/>
                    <Route path='/books/:id' component={BookEdit}/>
                    <Route path='/authors' exact={true} component={AuthorList}/>
                    <Route path='/authors/:id' component={AuthorEdit}/>
                    <Route path='/styles' exact={true} component={StyleList}/>
                    <Route path='/styles/:id' component={StyleEdit}/>
                    <Route path='/comments/:id' component={Comments}/>
                </Switch>
            </Router>
        )
    }
}

export default App