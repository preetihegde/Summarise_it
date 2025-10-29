document.addEventListener('DOMContentLoaded', () => {
    chrome.storage.local.get(['Notes'], function(result){
        if(result.Notes){
            document.getElementById('notes').value = result.Notes;
        }
    });

    document.getElementById('summariseBtn').addEventListener('click', summariseText)
    document.getElementById('savenotesBtn').addEventListener('click', saveNotes)
});

async function summariseText(){
    try{
        //get users active tab
        const [tab] = await chrome.tabs.query({active:true, currentWindow: true});
        //from that active tab get the selected text
        const [{result}] = await chrome.scripting.executeScript({
            target: {tabId: tab.id},
            function: () => window.getSelection().toString()
        });

        if(!result){
            showResult('Please select some text first')
            return;
        }

        const response = await fetch('http://localhost:8080/api/summarise/process', {
            method: 'POST',
            headers:{ 'content-Type' : 'application/json'},
            body: JSON.stringify({content: result, operation: 'summarise'})
        });

        if(!response.ok) {
            throw new Error('API Error: $(response.status)');
        }

        const text = await response.text();
        showResult(text.replace(/\n/g,'<br>'));

    }catch(error){
        showResult('Error: ' + error.message);
    }
    
}

async function saveNotes(){
    const notes = document.getElementById('notes').value;
    chrome.storage.local.set({'Notes': notes}, function(){
        alert('Notes svaed successfully');
    })
}


function showResult(content){
    document.getElementById('results').innerHTML = `<div class="result-item"><div class="result-content">${content}</div></div>`;
}
