import React, {Component} from 'react';
import {withRouter} from 'react-router-dom';
import {Button, Container, FormGroup, Input} from '@material-ui/core';
import Select from 'react-select'
import {Form, Label} from "reactstrap";


class StyleEdit extends Component {

    emptyItem = {
        style: '',
        books: []
    };

    constructor(props) {
        super(props);
        this.state = {
            books: [],
            initialBooks: [],
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleBooksSelectChange = this.handleBooksSelectChange.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const style = await (await fetch(`/style/${this.props.match.params.id}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            })).json();
            this.setState({item: style});
            this.setState({initialBooks: style.books})
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

        await fetch(item.id ? '/style/' + item.id : '/style', {
            method: item.id ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });

        for (let i = 0; i < item.books.length; i++){
            await fetch('/book/style/link', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({isbn: item.books[i].id, style: item.style}),
            });
        }

        let bookIds = item.books.map(book => book.id)
        let booksForUnlink = initialBooks.filter(book => bookIds.indexOf(book.id) === -1)

        for (let i = 0; i < booksForUnlink.length; i++) {
            await fetch('/book/style/unlink', {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({isbn: booksForUnlink[i].id, style: item.style}),
            });
        }

        this.props.history.push('/styles');
    }

    render() {
        const {books, item} = this.state;
        const title = <h2>{item.id ? 'Изменение жанра' : 'Добавление жанра'}</h2>;
        const bookNames = item.books.map(book => {
            return `${book.name || ''} (${book.publishedYear || ''})`;
        })
        let bookOptions = books.filter(function (book) {
                return bookNames.indexOf(book.label) === -1;
            }
        )
        return <div>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="style">Название жанра</Label>
                        <Input type="text" name="style" id="style" value={item.style || ''}
                               onChange={this.handleChange} autoComplete="style"/>
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
                        <Button color="secondary" href="/styles">Назад</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withRouter(StyleEdit);