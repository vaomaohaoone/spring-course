import React, {Component} from 'react';
import {withRouter} from 'react-router-dom';
import {Button, Container, FormGroup, Input, TextField} from '@material-ui/core';
import Select from 'react-select'
import {Form, Label} from "reactstrap";

class BookEdit extends Component {

    emptyItem = {
        name: '',
        publishedYear: '',
        authors: [],
        styles: []
    };

    constructor(props) {
        super(props);
        this.state = {
            authors: [],
            styles: [],
            initialAuthors: [],
            initialStyles: [],
            item: this.emptyItem
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleAuthorsSelectChange = this.handleAuthorsSelectChange.bind(this);
        this.handleStyleSelectChange = this.handleStyleSelectChange.bind(this);
    }

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const book = await (await fetch(`/book/${this.props.match.params.id}`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            })).json();
            this.setState({item: book});
            this.setState({initialAuthors: book.authors})
            this.setState({initialStyles: book.styles})
        }

        const authors = await (await fetch(`/author`, {
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                }
            }
        )).json();
        this.setState({
            authors: authors.map(author => {
                const authorName = `${author.name || ''} ${author.surname || ''}`;
                return {value: author, label: authorName}
            })
        });

        const styles = await (await fetch(`/style`, {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })).json();
        this.setState({
            styles: styles.map(style => {
                const type = `${style.style || ''}`;
                return {value: style, label: type}
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

    handleAuthorsSelectChange(option) {
        let item = {...this.state.item};
        if (option == null)
            item.authors = []
        else {
            item.authors = []
            for (let i = 0, l = option.length; i < l; i++) {
                item.authors.push(option[i].value);
            }
        }
        this.setState({item});
    }

    handleStyleSelectChange(option) {
        let item = {...this.state.item};
        if (option == null)
            item.styles = []
        else {
            item.styles = []
            for (let i = 0, l = option.length; i < l; i++) {
                item.styles.push(option[i].value);
            }
        }
        this.setState({item});
    }

    async handleSubmit(event) {
        event.preventDefault();
        const {item, initialAuthors, initialStyles} = this.state;

        const book = await (await fetch(item.id ? '/book/' + item.id : '/book', {
            method: item.id ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        })).json();

        for (let i = 0; i < item.authors.length; i++) {
            await fetch('/book/author/link', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({isbn: book.id, authorId: item.authors[i].id})
            });
        }

        let authorIds = item.authors.map(author => author.id)
        let authorsForUnlink = initialAuthors.filter(author => authorIds.indexOf(author.id) === -1)

        for (let i = 0; i < authorsForUnlink.length; i++) {
            await fetch('/book/author/unlink', {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({isbn: book.id, authorId: authorsForUnlink[i].id})
            });
        }

        for (let i = 0; i < item.styles.length; i++) {
            await fetch('/book/style/link', {
                method: 'POST',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({isbn: book.id, style: item.styles[i].style})
            });
        }

        let styleIds = item.styles.map(style => style.id)

        let stylesForUnlink = initialStyles.filter(style => styleIds.indexOf(style.id) === -1)

        for (let i = 0; i < stylesForUnlink.length; i++) {
            await fetch('/book/style/unlink', {
                method: 'DELETE',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({isbn: book.id, style: stylesForUnlink[i].style})
            });
        }

        this.props.history.push('/books');
    }

    render() {
        const {authors, styles, item} = this.state;
        const title = <h2>{item.id ? 'Изменение книги' : 'Добавление книги'}</h2>;
        const authorNames = item.authors.map(author => {
            return `${author.name || ''} ${author.surname || ''}`;
        })
        const styleNames = item.styles.map(style => {
            return style.style;
        })
        let authorOptions = authors.filter(function (author) {
                return authorNames.indexOf(author.label) === -1;
            }
        )
        let styleOptions = styles.filter(function (style) {
                return styleNames.indexOf(style.label) === -1;
            }
        )
        return <div>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <FormGroup>
                        <Label for="name">Название книги</Label>
                        <TextField name="name" variant={"outlined"} id="name" value={item.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="publishedYear">Год публикации</Label>
                        <Input type="text" pattern="^\d{4}$" name="publishedYear" id="publishedYear"
                               value={item.publishedYear || ''}
                               onChange={this.handleChange} autoComplete="publishedYear"/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="authors">Авторы</Label>
                        <Select isMulti closeMenuOnSelect={false} options={authorOptions}
                                value={item.authors.map(author => {
                                    const authorName = `${author.name || ''} ${author.surname || ''}`;
                                    return {value: author, label: authorName}
                                })}
                                onChange={this.handleAuthorsSelectChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Label for="styles">Жанры</Label>
                        <Select isMulti closeMenuOnSelect={false} options={styleOptions}
                                value={item.styles.map(style => {
                                    return {value: style, label: style.style}
                                })}
                                onChange={this.handleStyleSelectChange}/>
                    </FormGroup>
                    <FormGroup>
                        <Button color="primary" type="submit">Сохранить</Button>{' '}
                        <Button color="secondary" href="/books">Назад</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }
}

export default withRouter(BookEdit);