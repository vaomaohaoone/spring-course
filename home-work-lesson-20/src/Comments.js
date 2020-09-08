import React, {Component} from "react";
import {withRouter} from "react-router-dom";
import './Comments.scss';

class CommentBox extends Component {
    constructor(props) {
        super(props);

        this.state = {
            showComments: false,
            comments: [],
            bookLabel: '',
            isLoading: true
        };
    }

    async componentDidMount() {
        this.setState({isLoading: true});

        const book = await (await fetch(`/book/${this.props.match.params.id}`, {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        })).json();
        this.setState({bookLabel: `${book.name || ''} (${book.publishedYear || ''})`})


        await (await fetch('/comment/isbn/' + this.props.match.params.id, {
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        }))
            .json()
            .catch(err => {
                console.log("Error reading data: " + err)
            })
            .then(data => this.setState({comments: data, isLoading: false}));
    }

    render() {
        const comments = this._getComments();
        let commentNodes;
        let buttonText = 'Показать комментарии';

        if (this.state.showComments) {
            buttonText = 'Скрыть комментарии';
            commentNodes = <div className="comment-list">{comments}</div>;
        }

        return (
            <div className="comment-box">
                <h2><a href="/books" className="comment-back">Назад</a></h2>
                <h2>Комментарии для книги: {this.state.bookLabel}</h2>
                <CommentForm addComment={this._addComment.bind(this)}/>
                <button id="comment-reveal" onClick={this._handleClick.bind(this)}>
                    {buttonText}
                </button>
                <h3>Комментарии</h3>
                <h4 className="comment-count">
                    {this._getCommentsTitle(comments.length)}
                </h4>
                {commentNodes}
            </div>
        );
    } // end render

    async _addComment(body) {
        const comment = await (await fetch('/comment/add', {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({text: body, isbn: this.props.match.params.id}),
        })).json();
        this.setState({comments: this.state.comments.concat([comment])}); // *new array references help React stay fast, so concat works better than push here.
    }

    _handleClick() {
        this.setState({
            showComments: !this.state.showComments
        });
    }

    handleDelete = commentId => {
        const comments = this.state.comments.filter(comment => comment.id !== commentId);
        this.setState({comments: comments})
        fetch('/comment/' + commentId, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        })
    }

    _getComments() {
        return this.state.comments.map((comment) => {
            return (
                <React.Fragment>
                    <Comment
                        value={comment.text}
                        key={comment.id}
                        id={comment.id}
                        onDelete={this.handleDelete}
                    />
                </React.Fragment>
            );
        });
    }

    _getCommentsTitle(commentCount) {
        if (commentCount === 0) {
            return 'Комментариев ещё нет';
        } else if (commentCount === 1) {
            return "1 комментарий";
        } else {
            return `${commentCount} комментария(ев)`;
        }
    }
} // end CommentBox component

export default withRouter(CommentBox);

class CommentForm extends Component {
    render() {
        return (
            <form className="comment-form" onSubmit={this._handleSubmit.bind(this)}>
                <div className="comment-form-fields">
                    <textarea placeholder="Введите текст" rows="4" required
                              ref={(textarea) => this._body = textarea}></textarea>
                </div>
                <div className="comment-form-actions">
                    <button type="submit">Добавить</button>
                </div>
            </form>
        );
    } // end render

    _handleSubmit(event) {
        event.preventDefault();   // prevents page from reloading on submit
        let body = this._body;
        this.props.addComment(body.value);
    }
} // end CommentForm component

class Comment extends Component {
    constructor(props) {
        super(props);
        console.log("Create comment")
    }

    render() {
        return (
            <React.Fragment>
                <div className="comment">
                    <p className="comment-body">- {this.props.value}</p>
                    <div className="comment-footer">
                        <button className="comment-footer-delete"
                                onClick={() => this.props.onDelete(this.props.id)}>Удалить
                        </button>
                    </div>
                </div>
            </React.Fragment>
        );
    }
}
