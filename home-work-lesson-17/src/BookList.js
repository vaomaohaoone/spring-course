import React, {Component} from 'react';
import {ButtonGroup} from 'reactstrap';
import {Button, TableContainer, Table, Link} from '@material-ui/core';
import Paper from "@material-ui/core/Paper";

class BookList extends Component {
    constructor(props) {
        super(props);
        this.state = {books: [], isLoading: true};
    }

    componentDidMount() {
        this.setState({isLoading: true});

        //let books = [];

        fetch('book', {headers : {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .catch(err => {
                console.log("Error reading data: " + err)
            })
            .then(data => this.setState({books: data, isLoading: false}));
    }

    async remove(id) {
        await fetch(`/book/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedBooks = [...this.state.books].filter(i => i.id !== id);
            this.setState({books: updatedBooks});
        });
    }

    render() {

        const {books, isLoading} = this.state;

        if (isLoading) {
            return <p>Loading...</p>;
        }

        const bookList = books.map(book => {
            const authors = book.authors.map(function (author) {
                return author["name"] + " " + author["surname"]
            }).join(",")
            const styles = book.styles.map(function (style) {
                return style["style"]
            }).join(",")
            return <tr key={book.id}>
                <td>{book.name}</td>
                <td>{book.publishedYear}</td>
                <td>{authors}</td>
                <td>{styles}</td>
                <td>
                    <ButtonGroup>
                        <Button variant="contained" color={"inherit"} href={"/books/" + book.id}>Правка</Button>
                        <Button variant="contained" color={"inherit"} href={"/comments/" + book.id}>Комментарии</Button>
                        <Button variant="contained" color={"secondary"} onClick={() => this.remove(book.id)}>Удалить</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <TableContainer component={Paper} >
                    <div className="float-right">
                        <Button variant="contained" color={"primary"} href="/books/new">Добавить книгу</Button>
                    </div>
                    <h3>Книги</h3>
                    <Table>
                        <thead>
                        <tr>
                            <td> Название </td>
                            <td> Год издания </td>
                            <td> Авторы </td>
                            <td> Жанры </td>
                        </tr>
                        </thead>
                        <tbody>
                        {bookList}
                        </tbody>
                    </Table>
                    <Button color="link"><Link href="/">Назад</Link></Button>
                </TableContainer>
            </div>
        );
    }
}

export default BookList;