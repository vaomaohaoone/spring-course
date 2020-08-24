import React, {Component} from 'react';
import ButtonGroup from "@material-ui/core/ButtonGroup";
import Button from "@material-ui/core/Button";
import TableContainer from "@material-ui/core/TableContainer";
import Table from "@material-ui/core/Table";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import Link from "@material-ui/core/Link";

class StyleList extends Component {
    constructor(props) {
        super(props);
        this.state = {
            styles: [],
            isLoading: true
        };
    }

    componentDidMount() {
        this.setState({isLoading: true});

        fetch('style', {headers : {
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

    render() {

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
                        <Button variant="contained" color={"secondary"} onClick={() => this.remove(style.id)}>Удалить</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        return (
            <div>
                <TableContainer fluid >
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
    }
}

export default StyleList;