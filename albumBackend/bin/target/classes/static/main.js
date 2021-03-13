document.querySelector("#postForm").addEventListener("submit", postData);
document.querySelector("#createButton").addEventListener("click", showCreate);
document.querySelector("#changeForm").addEventListener("submit", editData);
document.querySelector("#deleteButton").addEventListener("click", deleteAlbum);
//deleteButton.addEventListener("click", () => deleteAlbum);
let currentId = 0;

getAlbums();

function getAlbums() {
	let stack = document.querySelector("#stack");
	stack.innerHTML = "";
	axios.get("/albums")
	.then(res => {
		const albums = res.data;
		albums.forEach(album => {
			let imgSrc = "";
			let dfltImgSrc = "turntabled1.jpg";
			if (album.imgSrc == "") {
				imgSrc = dfltImgSrc;
			} else {
				imgSrc = album.imgSrc;
				noImgLbl = ""
			}
			let html = document.createElement("div");
			html.setAttribute("class", "card");
			html.innerHTML = '<div class="image"><img src="'+ imgSrc +'"></div><div class="extra content"><div class="header">'+ album.title +'</div><div class="meta">Released in '+ album.releaseYear +'</div><div class="description">'+ album.artist +'</div><span class="right floated"><button class="ui icon button edit" onclick="yeet('+ album.id +')"><i class="pencil alternate icon"></i></button><a class="ui icon button playBtn" href="'+ album.playSrc +'"target="_blank"><i class="play icon"></i></a></span></div>';
			stack.appendChild(html);				
		});
	}).catch(err => console.error(err));
}

function showCreate(){
	$(".modalForm").modal("show");
}

// show edit form modal on button onclick 
function yeet(id){
	currentId = id;
	$(".editForm").modal("show");
	axios.get("/album/" + currentId)
	.then(res => {
		const chosenAlbum = res.data;
		document.getElementById("editTitle").value = chosenAlbum.title;
		document.getElementById("editArtist").value = chosenAlbum.artist;
		document.getElementById("editReleaseYear").value = chosenAlbum.releaseYear;
		document.getElementById("editImgSrc").value = chosenAlbum.imgSrc;
		document.getElementById("editPlaySrc").value = chosenAlbum.playSrc;						
	});
}	
	
function postData() {
	const data = {
		title: this.title.value,
		artist: this.artist.value,
		releaseYear: this.releaseYear.value,
		imgSrc: this.imgSrc.value,
		playSrc: this.playSrc.value
	};
	axios.post("/album", data, {
		headers: {
		"Content-Type": "application/json", 
		"Accept": "application/json"
		}
	})
	.then(() => getAlbums())
	.catch(err => console.error(err));
	}

function editData() {
	const data = {
		title: this.title.value,
		artist: this.artist.value,
		releaseYear: this.releaseYear.value,
		imgSrc: this.imgSrc.value,
		playSrc: this.playSrc.value
	};
	console.log(currentId);
	axios.put("/album/" + currentId, data, {
		headers: {
		"Content-Type": "application/json", 
		"Accept": "application/json"
		}
	})
	.then(() => getAlbums())
	.catch(err => console.error(err));
}


function deleteAlbum() {
	console.log(currentId)
	axios.delete("/album/" + currentId)
		.then(() => getAlbums())
		.catch(err => console.error(err));
};
	
	 