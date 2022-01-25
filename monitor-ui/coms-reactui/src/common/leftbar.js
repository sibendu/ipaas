import React , { useState ,useEffect } from 'react';
import ReactDOM from 'react-dom';
function LeftBar(props) {
    const handleClick = (page)=> {
        console.log('page1 clicked:'+ page);
        props.onClick(page);
      } 
    return (
        <nav id="sidebar">
        <div class="sidebar-header">
            <h3>COMS</h3>
            <strong>COMS</strong>
        </div>

        <ul class="list-unstyled components">
            <li class="active">
                <a href="#homeSubmenu" data-toggle="collapse" aria-expanded="false" class="dropdown-toggle">
                    <i class="fas fa-home"></i>
                    Home
                </a>
                <ul class="collapse list-unstyled" id="homeSubmenu">
                    <li>
                        <a href="#" onClick={()=>handleClick("Home")}>Home</a>
                    </li>
                    <li>
                        <a href="#" onClick={()=>handleClick("CustomerAllView")}>View All Customer</a>
                    </li>
                   
                </ul>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-briefcase"></i>
                    About
                </a>
            </li>
            <li>
                <a href="#">
                    <i class="fas fa-paper-plane"></i>
                    Contact
                </a>
            </li>
        </ul>

    </nav>
    )
}
export default LeftBar;