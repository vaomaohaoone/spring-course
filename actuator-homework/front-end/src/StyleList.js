import React, {Component} from 'react';
import ButtonGroup from "@material-ui/core/ButtonGroup";
import Button from "@material-ui/core/Button";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import Link from "@material-ui/core/Link";
import Grid from "@material-ui/core/Grid";
import BottomNavigation from "@material-ui/core/BottomNavigation";
import BottomNavigationAction from "@material-ui/core/BottomNavigationAction";
import ExitToAppIcon from "@material-ui/icons/ExitToApp";
import {Redirect} from "react-router-dom";

class StyleList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            styles: [],
            isLoading: true,
            loggedOut: false
        };
        this.onLogout = this.onLogout.bind(this);
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('style', {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })
            .then(response => response.json())
            .catch(err => {
                console.log("Error reading data: " + err)
            })
            .then(data => this.setState({styles: data, isLoading: false}));
    }

    async remove(id) {
        await fetch(`/style/${id}`, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedStyles = [...this.state.styles].filter(i => i.id !== id);
            this.setState({styles: updatedStyles});
        });
    }

    onLogout = async () => {
        await (await fetch('logout').then(() => {
            localStorage.setItem("authenticated", "false")
            this.setState({loggedOut: true})
        }))
    }

    render() {
        if (localStorage.getItem("authenticated") != null && localStorage.getItem("authenticated") === "true" && this.state.loggedOut !== true) {

            const {styles, isLoading} = this.state;

            if (isLoading) {
                return <p>Loading...</p>;
            }

            const styleList = styles.map(style => {
                const books = style.books.map(function (book) {
                    return book["name"] + ", год издания: " + book["publishedYear"]
                }).join(",")
                return <tr key={style.id}>
                    <td>{style.style}</td>
                    <td>
                        {books}
                    </td>
                    <td>
                        <ButtonGroup>
                            <Button variant="contained" color={"inherit"} href={"/styles/" + style.id}>Правка</Button>
                            <Button variant="contained" color={"secondary"}
                                    onClick={() => this.remove(style.id)}>Удалить</Button>
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
                    <TableContainer fluid>
                        <div className="float-right">
                            <Button variant="contained" color={"primary"} href="/styles/new">Добавить жанр</Button>
                        </div>
                        <h3>Жанры</h3>
                        <Table>
                            <TableHead>
                                <TableRow>
                                    <TableCell align="left">Жанр</TableCell>
                                    <TableCell align="left">Книги</TableCell>
                                </TableRow>
                            </TableHead>
                            <tbody>
                            {styleList}
                            </tbody>
                        </Table>
                        <Button color="link"><Link href="/">Назад</Link></Button>
                    </TableContainer>
                </div>
            );
        } else {
            return (<Redirect to={'/'}/>)
        }
    }
}

export default StyleList;