import React, {Component} from 'react';
import {Redirect, withRouter} from 'react-router-dom';
import {Button, Container, FormGroup, Input} from '@material-ui/core';
import {Form, Label} from "reactstrap";
import Select from 'react-select'
import Grid from "@material-ui/core/Grid";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";

class AuthorEdit extends Component {

    emptyItem = {
        name: '',
        surname: '',
        books: []
    };

    constructor(props) {
        super(props);
        this.state = {
            books: [],
            initialBooks: [],
            item: this.emptyItem,
            loggedOut: false
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleBooksSelectChange = this.handleBooksSelectChange.bind(this);
        this.onLogout = this.onLogout.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const author = await (await fetch(`/author/${this.props.match.params.id}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            })).json();
            this.setState({item: author});
            this.setState({initialBooks: author.books})
        }

        const books = await (await fetch(`/book`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            }
        )).json();
        this.setState({
            books: books.map(book => {
                const bookLabel = `${book.name || ''} (${book.publishedYear || ''})`;
                return {value: book, label: bookLabel}
            })
        });
    }

    handleChange(event) {
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};
        item[name] = value;
        this.setState({item});
    }

    handleBooksSelectChange(option) {
        let item = {...this.state.item};
        if (option == null)
            item.books = []
        else {
            item.books = []
            for (let i = 0, l = option.length; i < l; i++) {
                item.books.push(option[i].value);
            }
        }
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item, initialBooks} = this.state;

        let savedItem = await (await fetch(item.id ? '/author/' + item.id : '/author', {
            method: item.id ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        })).json();

        for (let i = 0; i < item.books.length; i++){
            await fetch('/book/author/link', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({isbn: item.books[i].id, authorId: savedItem.id})
            });
        }

        let bookIds = item.books.map(book => book.id)
        let booksForUnlink = initialBooks.filter(book => bookIds.indexOf(book.id) === -1)

        for (let i = 0; i < booksForUnlink.length; i++) {
            await fetch('/book/author/unlink', {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({isbn: booksForUnlink[i].id, authorId: item.id})
            });
        }

        this.props.history.push('/authors');
    }

    onLogout = async () => {
        await (await fetch('logout').then(() => {
            localStorage.setItem("authenticated", "false")
            this.setState({loggedOut: true})
        }))
    }

    render() {
        if (localStorage.getItem("authenticated") != null && localStorage.getItem("authenticated") === "true" && this.state.loggedOut !== true ) {

            const {books, item} = this.state;
            const title = <h2>{item.id ? 'Изменение автора' : 'Добавление автора'}</h2>;
            const bookNames = item.books.map(book => {
                return `${book.name || ''} (${book.publishedYear || ''})`;
            })
            let bookOptions = books.filter(function (book) {
                    return bookNames.indexOf(book.label) === -1;
                }
            )
            return <div>
                <Grid container alignItems="flex-start" justify="flex-end" direction="row">
                    <BottomNavigation showLabels={true}>
                        <BottomNavigationAction component={Button} label={"Выход"} value={"Выход"}
                                                icon={<ExitToAppIcon/>} onClick={this.onLogout}/>
                    </BottomNavigation>
                </Grid>
                <Container>
                    {title}
                    <Form onSubmit={this.handleSubmit}>
                        <FormGroup>
                            <Label for="name">Имя</Label>
                            <Input type="text" name="name" id="name" value={item.name || ''}
                                   onChange={this.handleChange} autoComplete="name"/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="surname">Фамилия</Label>
                            <Input type="text" name="surname" id="surname" value={item.surname || ''}
                                   onChange={this.handleChange} autoComplete="surname"/>
                        </FormGroup>
                        <FormGroup>
                            <Label for="books">Книги</Label>
                            <Select isMulti closeMenuOnSelect={false} options={bookOptions}
                                    value={item.books.map(book => {
                                        const bookLabel = `${book.name || ''} (${book.publishedYear || ''})`;
                                        return {value: book, label: bookLabel}
                                    })}
                                    onChange={this.handleBooksSelectChange}/>
                        </FormGroup>
                        <FormGroup>
                            <Button color="primary" type="submit">Сохранить</Button>{' '}
                            <Button color="secondary" href="/authors">Удалить</Button>
                        </FormGroup>
                    </Form>
                </Container>
            </div>
        }
        else {
            return (<Redirect to={'/'}/>)
        }
    }
}

export default withRouter(AuthorEdit);