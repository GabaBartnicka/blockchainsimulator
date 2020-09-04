import React from "react";
import {Link} from "react-router-dom";

export const CustomLink: any = React.forwardRef((props: any, ref: any) => (
    <Link ref={ref} to={props.to} {...props}>{props.children}</Link>
))
