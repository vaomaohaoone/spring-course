import React, {Component} from 'react';
import ButtonGroup from "@material-ui/core/ButtonGroup";
import Button from "@material-ui/core/Button";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import Paper from "@material-ui/core/Paper";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import Link from "@material-ui/core/Link";
import {Redirect} from "react-router-dom";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import Grid from "@material-ui/core/Grid";

class AuthorList extends Component {
    constructor(props) {
        super(props);
        this.state = {authors: [], isLoading: true, loggedOut: false};
        this.onLogout = this.onLogout.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});


        fetch('author', {headers : {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .catch(err => {
                console.log("Error reading data: " + err)
            })
            .then(data => this.setState({authors: data, isLoading: false}));
    }

    async remove(id) {
        await fetch(`/author/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedAuthors = [...this.state.authors].filter(i => i.id !== id);
            this.setState({authors: updatedAuthors});
        });
    }

    onLogout = async () => {
        await (await fetch('logout').then(() => {
            localStorage.setItem("authenticated", "false")
            this.setState({loggedOut: true})
        }))
    }

    render() {
        if (localStorage.getItem("authenticated") != null && localStorage.getItem("authenticated") === "true" && this.state.loggedOut !== true ) {
            const {authors, isLoading} = this.state;

            if (isLoading) {
                return <p>Loading...</p>;
            }

            const authorList = authors.map(author => {
                const books = author.books.map(function (book) {
                    return book["name"] + ", год издания: " + book["publishedYear"]
                })
                return <tr key={author.id}>
                    <td>{author.name}</td>
                    <td>{author.surname}</td>
                    <td>
                        {books}
                    </td>
                    <td>
                        <ButtonGroup>
                            <Button variant="contained" color={"inherit"} href={"/authors/" + author.id}>Правка</Button>
                            <Button variant="contained" color={"secondary"}
                                    onClick={() => this.remove(author.id)}>Удалить</Button>
                        </ButtonGroup>
                    </td>
                </tr>
            });

            return (
                <div>
                    <Grid container alignItems="flex-start" justify="flex-end" direction="row">
                        <BottomNavigation showLabels={true}>
                            <BottomNavigationAction component={Button} label={"Выход"} value={"Выход"}
                                                    icon={<ExitToAppIcon/>} onClick={this.onLogout}/>
                        </BottomNavigation>
                    </Grid>
                    <TableContainer component={Paper}>
                        <div className="float-right">
                            <Button variant="contained" color={"primary"} href="/authors/new">Добавить автора</Button>
                        </div>
                        <h3>Авторы</h3>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell align="left">Имя</TableCell>
                                    <TableCell align="left">Фамилия</TableCell>
                                    <TableCell align="left">Книги</TableCell>
                                </TableRow>
                            </TableHead>
                            <tbody>
                            {authorList}
                            </tbody>
                        </Table>
                        <Button color="link"><Link href="/">Назад</Link></Button>
                    </TableContainer>
                </div>
            );
        }
        else {
            return (<Redirect to={'/'}/>)
        }
    }
}

export default AuthorList;